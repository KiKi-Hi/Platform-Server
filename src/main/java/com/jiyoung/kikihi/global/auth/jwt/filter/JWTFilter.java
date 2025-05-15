package com.jiyoung.kikihi.global.auth.jwt.filter;

import com.jiyoung.kikihi.global.auth.jwt.user.JWTUserDetails;
import com.jiyoung.kikihi.global.auth.jwt.util.JWTExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.jiyoung.kikihi.global.common.response.ErrorCode.*;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTExtractor jwtExtractor;
    private final static String JWT_ERROR = "jwtError";

    // doFilterInternal
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 추출
        Optional<String> token = jwtExtractor.extractToken(request);

        // 토큰 검증 (null인지 validate한지 만료되었는지)
        if (token.isEmpty()) {
            request.setAttribute(JWT_ERROR, TOKEN_NOT_FOUND);
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = token.get();
        if (!jwtExtractor.validateToken(accessToken)) {
            request.setAttribute(JWT_ERROR, TOKEN_INVALID);
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtExtractor.isExpired(accessToken)) {
            request.setAttribute(JWT_ERROR, TOKEN_EXPIRED);
            filterChain.doFilter(request, response);
            return;
        }
        // 권한 생성하기
        setAuthenticationToContext(accessToken);
        filterChain.doFilter(request, response);

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
        SecurityContextHolder.getContext().setAuthentication(authentication);


    }
}
