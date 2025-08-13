package site.kikihi.custom.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
/**
 * CORS 설정을 위한 Config 입니다.
 */
@Configuration
public class CorsConfig {

    @Value("${spring.front.host}")
    private String frontHost;

    @Value("${spring.front.prod}")
    private String frontProd;

    @Value("${spring.back.prod}")
    private String backProd;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        /// CORS 추가
        configuration.addAllowedOriginPattern(frontHost);
        configuration.addAllowedOriginPattern(frontProd);
        configuration.addAllowedOriginPattern(backProd);

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
