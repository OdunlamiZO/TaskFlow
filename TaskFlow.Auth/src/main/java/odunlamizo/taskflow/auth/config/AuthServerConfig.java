package odunlamizo.taskflow.auth.config;

import java.time.Duration;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import odunlamizo.taskflow.auth.jose.Jwks;
import odunlamizo.taskflow.auth.service.UserInfoService;

@Configuration(proxyBeanMethods = false)
public class AuthServerConfig {

    @Value("${odunlamizo.taskflow.jwt.issuer}")
    private String issuer;

    @Value("${odunlamizo.taskflow.web.secret}")
    private String webSecret;

    @Value("${odunlamizo.taskflow.web.url}")
    private String webUrl;

    @Value("${odunlamizo.taskflow.cli.url}")
    private String cliUrl;

    @Value("${odunlamizo.taskflow.access-token-time-to-live}")
    private Duration accessTokenTimeToLive;

    @Value("${odunlamizo.taskflow.refresh-token-time-to-live}")
    private Duration refreshTokenTimeToLive;

    @Autowired
    private UserInfoService userInfoServive;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate, PasswordEncoder encoder) {
        RegisteredClient webClient = RegisteredClient.withId("fb7b5be1-7a36-4c79-b9ed-85e0b290e8ab")
                .clientId("TaskFlow.Web")
                .clientSecret(encoder.encode(webSecret))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri(String.format("%s/login/oauth2/code/web", webUrl))
                .redirectUri(String.format("%s/authorized", webUrl))
                .scope("read:user")
                .scope("write")
                .scope("write:query")
                .tokenSettings(
                        TokenSettings.builder()
                                .accessTokenTimeToLive(accessTokenTimeToLive)
                                .refreshTokenTimeToLive(refreshTokenTimeToLive)
                                .build())
                .build();
        RegisteredClient cliClient = RegisteredClient.withId("131d9de5-472f-4605-a5c8-e3205f64e655")
                .clientId("TaskFlow.Cli")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(String.format("%s/login/oauth2/code/cli", cliUrl))
                .redirectUri(String.format("%s/authorized", cliUrl))
                .scope("read:user")
                .tokenSettings(
                        TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofDays(5)) // spring doesn't return refresh token for public client, so i made the access token long lived
                                .build())
                .build();
        JdbcRegisteredClientRepository jdbcRegisteredClientRepository = new JdbcRegisteredClientRepository(
                jdbcTemplate);
        jdbcRegisteredClientRepository.save(webClient);
        jdbcRegisteredClientRepository.save(cliClient);
        return jdbcRegisteredClientRepository;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            Authentication principal = context.getPrincipal();
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)
                    && !context.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
                context.getClaims().claim("role", principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
            }
            if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)
                    && !context.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
                context.getClaims().claims(claims -> {
                    OidcUserInfo userInfo = userInfoServive.loadUserInfo(principal.getName());
                    if (context.getAuthorizedScopes().contains("profile")) {
                        claims.put("given_name", userInfo.getGivenName());
                        claims.put("family_name", userInfo.getFamilyName());
                    }
                    if (context.getAuthorizedScopes().contains("email")) {
                        claims.put("email", userInfo.getEmail());
                        claims.put("email_verified", userInfo.getEmailVerified());
                    }
                });
            }
        };
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
            RegisteredClientRepository clientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, clientRepository);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer(issuer).build();
    }

}
