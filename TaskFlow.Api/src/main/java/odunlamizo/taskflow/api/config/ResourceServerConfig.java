package odunlamizo.taskflow.api.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import odunlamizo.taskflow.api.dto.Error;
import odunlamizo.taskflow.api.payload.response.AbstractResponsePayload;

import odunlamizo.taskflow.api.util.Json;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new Converter<Jwt, Collection<GrantedAuthority>>() {

                    @Override
                    @Nullable
                    public Collection<GrantedAuthority> convert(Jwt jwt) {
                        Collection<GrantedAuthority> authorities = new HashSet<>();
                        @SuppressWarnings("unchecked")
                        Collection<String> roles = (Collection<String>) jwt.getClaims().get("role");
                        if (roles != null) {
                            for (String role : roles) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                            }
                        }
                        Collection<String> scopes = (Collection<String>) getScopes(jwt);
                        if (!scopes.isEmpty()) {
                            for (String scope : scopes) {
                                authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
                            }
                        }
                        return authorities;
                    }

                    @SuppressWarnings("unchecked")
                    private Collection<String> getScopes(Jwt jwt) {
                        Collection<String> scopes = new HashSet<>();
                        for (String scope : (ArrayList<String>) jwt.getClaims().get("scope")) {
                            scopes.add(scope);
                        }
                        return scopes;
                    }

                });
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/username/update").hasAuthority("SCOPE_write:query")
                        .requestMatchers("/user/**").hasAuthority("SCOPE_read:user")
                        .requestMatchers("/task/add", "/task/{id:[\\d+]}/update", "/task/{id:[\\d+]}/delete", "/project/add", "/project/{id:[\\d+]}/update", "/project/{id:[\\d+]}/delete").hasAuthority("SCOPE_write")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFacade authenticationFacade() {
        return new AuthenticationFacade() {

            @Override
            public Jwt getPrincipal() {
                return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }

        };
    }

    public static interface AuthenticationFacade {

        Jwt getPrincipal();

    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
                HttpStatus status = HttpStatus.UNAUTHORIZED;
                String message = exception.getMessage();
                Map<String, String> parameters = new LinkedHashMap<>();
                /*
                 * if (realmName != null) {
                 * parameters.put("realm", realmName);
                 * }
                 */
                if (exception instanceof OAuth2AuthenticationException) {
                    OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
                    parameters.put("error", error.getErrorCode());
                    if (StringUtils.hasText(error.getDescription())) {
                        parameters.put("error_description", error.getDescription());
                    }
                    if (StringUtils.hasText(error.getUri())) {
                        parameters.put("error_uri", error.getUri());
                    }
                    if (error instanceof BearerTokenError) {
                        BearerTokenError bearerTokenError = (BearerTokenError) error;
                        if (StringUtils.hasText(bearerTokenError.getScope())) {
                            parameters.put("scope", bearerTokenError.getScope());
                        }
                        status = ((BearerTokenError) error).getHttpStatus();
                    }
                }
                String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
                response.setStatus(status.value());
                response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
                response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                Json.writeValue(
                        response.getOutputStream(),
                        AbstractResponsePayload.failure(
                                Error.builder()
                                        .useMessage(message)
                                        .build()));
            }

        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                    AccessDeniedException exception) throws IOException, ServletException {
                String message = exception.getMessage();
                Map<String, String> parameters = new LinkedHashMap<>();
                /*
                 * if (realmName != null) {
                 * parameters.put("realm", realmName);
                 * }
                 */
                if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken) {
                    parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
                    parameters.put("error_description",
                            "The request requires higher privileges than provided by the access token.");
                    parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
                }
                String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
                response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                Json.writeValue(
                        response.getOutputStream(),
                        AbstractResponsePayload.failure(
                                Error.builder()
                                        .useMessage(message)
                                        .build()));
            }

        };
    }

    private static String computeWWWAuthenticateHeaderValue(Map<String, String> parameters) {
        StringBuilder wwwAuthenticate = new StringBuilder();
        wwwAuthenticate.append("Bearer");
        if (!parameters.isEmpty()) {
            wwwAuthenticate.append(" ");
            int i = 0;
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
                if (i != parameters.size() - 1) {
                    wwwAuthenticate.append(", ");
                }
                i++;
            }
        }
        return wwwAuthenticate.toString();
    }

}
