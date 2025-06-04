package com.jiyoung.kikihi.global.config;


import com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationDeniedHandler;
import com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationFailureHandler;
import com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationFilter;
import com.jiyoung.kikihi.security.jwt.handler.JwtAuthenticationLogoutHandler;
import com.jiyoung.kikihi.security.oauth2.handler.OAuth2SuccessHandler;
import com.jiyoung.kikihi.security.oauth2.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final JwtAuthenticationDeniedHandler jwtAuthenticationDeniedHandler;

    private final JwtAuthenticationLogoutHandler jwtAuthenticationLogoutHandler;

    private final OAuth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oauth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Swagger 접근 허용
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        /// ! 항상 필터가 돈다. !
                        // 요청 허용
                        .anyRequest().permitAll()
                )
                // 필터 및 핸들러 처리
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(jwtAuthenticationFailureHandler)
                            .accessDeniedHandler(jwtAuthenticationDeniedHandler);
                })
                .oauth2Login(
                        oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                                oauth.userInfoEndpoint(c -> c.userService(oauth2UserService))
                                        // 로그인 성공 시 핸들러
                                        .successHandler(oauth2SuccessHandler)
                )
                /// 로그아웃
                .logout(this::configureLogout)

                /// 세션 무력화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /// 로그아웃 관련 설명
    private void configureLogout(LogoutConfigurer<HttpSecurity> logout) {
        logout
                // 1. 로그아웃 엔드포인트를 지정합니다.
                .logoutUrl("/api/v1/auth/logout")
                // 2. 엔드포인트 호출에 대한 처리 Handler를 구성합니다.
                .addLogoutHandler(jwtAuthenticationLogoutHandler)
                // 3. 로그아웃 처리가 완료되었을때 처리를 수행합니다.
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"로그아웃 성공\"}");
                });

    }


}
