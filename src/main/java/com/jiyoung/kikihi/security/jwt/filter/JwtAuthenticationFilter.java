package com.jiyoung.kikihi.security.jwt.filter;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.security.jwt.domain.JWTUserDetails;
import com.jiyoung.kikihi.security.jwt.util.JWTExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.jiyoung.kikihi.global.response.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTExtractor jwtExtractor;
    private final JwtAuthenticationFailureHandler failureHandler;

    public final static String JWT_ERROR = "jwtError";

    // doFilterInternal
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 추출
        try {
            Optional<String> token = jwtExtractor.extractToken(request);

            // 토큰 검증 (null인지 validate한지 만료되었는지)
            if (token.isEmpty()) {
                request.setAttribute(JWT_ERROR, TOKEN_NOT_FOUND);
                throw new JwtAuthenticationException(ErrorCode.TOKEN_NOT_FOUND.getMessage());
            }

            String accessToken = token.get();
            if (!jwtExtractor.validateToken(accessToken)) {
                request.setAttribute(JWT_ERROR, INVALID_TOKEN);
                throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN.getMessage());
            }

            if (jwtExtractor.isExpired(accessToken)) {
                request.setAttribute(JWT_ERROR, TOKEN_EXPIRED);
                throw new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED.getMessage());
            }
            // 권한 생성하기
            setAuthenticationToContext(accessToken);
            filterChain.doFilter(request, response);

        } catch (JwtAuthenticationException ex) {
            log.warn("JWT 인증 실패: {}", ex.getMessage());

            failureHandler.commence(request, response, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        // 필터를 적용하지 않을 URI 목록
        return uri.startsWith("/oauth2/**") ||
                uri.startsWith("/");
    }

    // 토큰 받아서 유저정보로 Authentication설정
    private void setAuthenticationToContext(String token) {

        // 토큰에서 유저정보 추출 (id, email, role)
        UUID id = jwtExtractor.getId(token);
        String email = jwtExtractor.getEmail(token);
        String role = jwtExtractor.getRole(token);

        // AuthenticationManager로 인증받기
        UserDetails userDetails = JWTUserDetails.of(id, email, role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        /// 시큐리티 컨텍스트에 저장하기
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }



}
