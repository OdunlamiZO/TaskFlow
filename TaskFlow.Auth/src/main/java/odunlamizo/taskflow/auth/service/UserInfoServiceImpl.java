package odunlamizo.taskflow.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import odunlamizo.taskflow.auth.model.User;
import odunlamizo.taskflow.auth.repository.UserRepository;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OidcUserInfo loadUserInfo(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();
        return OidcUserInfo.builder()
                .email(user.getEmail())
                .emailVerified(true)
                .givenName(user.getName().getFirstName())
                .familyName(user.getName().getLastName())
                .claim("role", String.valueOf(user.getRole()).toLowerCase())
                .build();
    }

}
