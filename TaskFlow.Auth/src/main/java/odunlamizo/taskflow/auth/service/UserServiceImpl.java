package odunlamizo.taskflow.auth.service;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import odunlamizo.taskflow.auth.dto.UserDto;
import odunlamizo.taskflow.auth.model.Name;
import odunlamizo.taskflow.auth.model.ProfilePhoto;
import odunlamizo.taskflow.auth.model.Role;
import odunlamizo.taskflow.auth.model.User;
import odunlamizo.taskflow.auth.repository.ProfilePhotoRepository;
import odunlamizo.taskflow.auth.repository.UserRepository;
import odunlamizo.taskflow.auth.util.Models;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Value("${odunlamizo.taskflow.mail.no-reply}")
    private String noReply;

    @Value("${odunlamizo.taskflow.resource.url}")
    private String resourceUrl;

    @Value("${odunlamizo.taskflow.web.url}")
    private String webUrl;

    @Value("${odunlamizo.taskflow.web.secret}")
    private String webSecret;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;

    @Override
    public String save(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(Models.stringToModel(userDto.getName(), Name.class));
        user.setUsername(userDto.getUsername());
        user.setRole(Role.USER);
        user.setPassword(encoder.encode(userDto.getPassword()));
        user = userRepository.save(user);
        WebClient webClient = WebClient.create(
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());

        return webClient.post()
                .uri(UriComponentsBuilder.fromPath("/mail/verification").queryParam("email", user.getEmail())
                        .toUriString())
                .exchangeToMono(response -> {
                    if (response.statusCode().is3xxRedirection()) {
                        return Mono.just(response.headers().header("Location").get(0));
                    } else {
                        return response.createError();
                    }
                })
                .block();
    }

    @Override
    public void updateProfile(UserDto _user, HttpServletRequest request) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(principal.getUsername()).get();
        user.setName(Models.stringToModel(_user.getName(), Name.class));
        if (user.getUsername().equals(_user.getUsername())) {
            userRepository.save(user);
        } else {
            WebClient.create(webUrl).post()
                    .uri(UriComponentsBuilder.fromPath("/token/revoke")
                            .queryParam("user", user.getUsername())
                            .queryParam("clean", true)
                            .toUriString())
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return Mono.empty();
                        } else {
                            return response.createError();
                        }
                    })
                    .block();
            Map<String, String> tokens = WebClient
                    .create(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()).post()
                    .uri(UriComponentsBuilder.fromPath("/oauth2/token").queryParam("grant_type", "client_credentials")
                            .queryParam("scope", "write:query")
                            .toUriString())
                    .header(HttpHeaders.AUTHORIZATION,
                            String.format("Basic %s",
                                    Base64.getEncoder().encodeToString(("TaskFlow.Web:" + webSecret).getBytes())))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                    })
                    .block();
            WebClient.create(resourceUrl).post()
                    .uri(UriComponentsBuilder.fromPath("/user/username/update")
                            .queryParam("username", String.format("%s,%s", user.getUsername(), _user.getUsername()))
                            .toUriString())
                    .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", tokens.get("access_token")))
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return Mono.empty();
                        } else {
                            return response.createError();
                        }
                    })
                    .block();
            user.setUsername(_user.getUsername());
            user = userRepository.save(user);
            Optional<ProfilePhoto> _profilePhoto = profilePhotoRepository.findByUser(principal.getUsername());
            if (_profilePhoto.isPresent()) {
                ProfilePhoto profilePhoto = _profilePhoto.get();
                profilePhoto.setUser(user.getUsername());
                profilePhotoRepository.save(profilePhoto);
            }
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(
                    new UsernamePasswordAuthenticationToken(loadUserByUsername(user.getUsername()),
                            principal.getPassword(), principal.getAuthorities()));
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> _user = userRepository.findByUsername(username);
        if (_user.isPresent()) {
            User user = _user.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(),
                    true,
                    true,
                    true,
                    Collections.singleton(new SimpleGrantedAuthority(String.valueOf(user.getRole()).toLowerCase())));
        }
        throw new UsernameNotFoundException(String.format("User[%s] not found.", username));
    }

}
