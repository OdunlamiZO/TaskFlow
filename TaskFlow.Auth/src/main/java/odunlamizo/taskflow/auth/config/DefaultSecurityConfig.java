package odunlamizo.taskflow.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

	@Autowired
	private CorsConfigurationSource corsConfigurationSource;

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.ignoringRequestMatchers("/login", "/signup", "/verify", "/mail/verification"))
				.cors(cors -> cors.configurationSource(corsConfigurationSource))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/login", "/signup", "/verify", "/user", "/mail/verification", "/css/**",
								"/images/**", "/js/**")
						.permitAll()
						.anyRequest().authenticated())
				.formLogin(login -> login
						.loginPage("/login")
						.usernameParameter("username")
						.passwordParameter("password"))
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/logout?success")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID"));
		return http.build();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
