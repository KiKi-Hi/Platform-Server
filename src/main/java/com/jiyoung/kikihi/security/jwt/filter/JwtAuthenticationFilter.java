package com.jiyoung.kikihi.security.jwt.filter;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.security.jwt.util.JWTExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

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
            var authentication = jwtExtractor.getAuthentication(token.get());

            /// 시큐리티 홀더에 해당 멤버 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("인증정보 :{}", authentication.getPrincipal().toString());

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
        return uri.startsWith("/oauth2/") ||
                uri.startsWith("/error") ||
                uri.startsWith("/login");
    }

}
