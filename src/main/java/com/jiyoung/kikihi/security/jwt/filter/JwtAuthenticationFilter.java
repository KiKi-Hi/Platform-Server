package com.jiyoung.kikihi.security.jwt.filter;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.security.jwt.exception.JwtAuthenticationException;
import com.jiyoung.kikihi.security.jwt.util.JwtTokenExtractor;
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

    private final JwtTokenExtractor extractor;
    private final JwtAuthenticationFailureHandler failureHandler;
    private final RequestMatcherHolder requestMatcherHolder;

    public final static String JWT_ERROR = "jwtError";

    // doFilterInternal
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 추출
        try {
            Optional<String> token = extractor.extractAccessToken(request);

            // 토큰 검증
            // 비어있는 지
            if (token.isEmpty()) {
                request.setAttribute(JWT_ERROR, TOKEN_NOT_FOUND);
                throw new JwtAuthenticationException(ErrorCode.TOKEN_NOT_FOUND.getMessage());
            }

            String accessToken = token.get();

            // 타당한지
            if (!extractor.validateToken(accessToken)) {
                request.setAttribute(JWT_ERROR, INVALID_TOKEN);
                throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN.getMessage());
            }

            // 만료가 안되었는지
            if (extractor.isExpired(accessToken)) {
                request.setAttribute(JWT_ERROR, TOKEN_EXPIRED);
                throw new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED.getMessage());
            }

            // 권한 생성하기
            var authentication = extractor.getAuthentication(token.get());

            /// 시큐리티 홀더에 해당 멤버 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (JwtAuthenticationException ex) {

            /// 실패 핸들러로 이동
            failureHandler.commence(request, response, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        /// null 인 것 해결
        boolean matches = requestMatcherHolder.getRequestMatchersByMinRole(null)
                .matches(request);

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.info("[로그] 주소  {}, 방식 {},결과 {}}", requestURI, method, matches);

        return matches;
    }

}
