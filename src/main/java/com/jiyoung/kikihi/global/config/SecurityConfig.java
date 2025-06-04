package com.jiyoung.kikihi.global.config;


import com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationDeniedHandler;
import com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationFailureHandler;
import com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationFilter;
import com.jiyoung.kikihi.security.jwt.filter.RequestMatcherHolder;
import com.jiyoung.kikihi.security.oauth2.handler.OAuth2SuccessHandler;
import com.jiyoung.kikihi.security.oauth2.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.jiyoung.kikihi.platform.domain.user.Role.ADMIN;
import static com.jiyoung.kikihi.platform.domain.user.Role.USER;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final JwtAuthenticationDeniedHandler jwtAuthenticationDeniedHandler;
    private final OAuth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oauth2SuccessHandler;

    private final RequestMatcherHolder requestMatcherHolder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(requestMatcherHolder.getRequestMatchersByMinRole(null))
                        .permitAll()
                        .requestMatchers(requestMatcherHolder.getRequestMatchersByMinRole(USER))
                        .hasAnyAuthority(USER.getRole(), ADMIN.getRole())
                        .requestMatchers(requestMatcherHolder.getRequestMatchersByMinRole(ADMIN))
                        .hasAnyAuthority(ADMIN.getRole())
                        .anyRequest().authenticated()
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
                /// 세션 무력화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
