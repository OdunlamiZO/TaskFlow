package odunlamizo.taskflow.auth.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${odunlamizo.taskflow.cors.allowed-origins}") 
    private String[] allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config_1 = new CorsConfiguration();
        config_1.setAllowedOrigins(Arrays.asList("*"));
        config_1.setAllowedMethods(Arrays.asList("POST"));
        config_1.setAllowedHeaders(Arrays.asList("*"));
        source.registerCorsConfiguration("/verify", config_1);
        CorsConfiguration config_2 = new CorsConfiguration();
        config_2.setAllowedOrigins(Arrays.asList(allowedOrigins));
        config_2.setAllowedMethods(Arrays.asList("POST", "GET"));
        config_2.setAllowedHeaders(Arrays.asList("*"));
        config_2.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config_2);
        return source;
    }
    
}
