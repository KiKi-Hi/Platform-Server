package com.jiyoung.kikihi.security.jwt.handler;

import com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationException;
import com.jiyoung.kikihi.security.jwt.util.JWTExtractor;
import com.jiyoung.kikihi.security.jwt.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.jiyoung.kikihi.global.response.ErrorCode.*;
import static com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationFilter.JWT_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationLogoutHandler implements LogoutHandler {

    private final JWTExtractor jwtExtractor;
    private final RedisUtil redisUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("[+] 로그아웃이 수행됩니다.");
        try {
            Optional<String> token = jwtExtractor.extractToken(request);

            if (token.isEmpty()) {
                request.setAttribute(JWT_ERROR, TOKEN_NOT_FOUND);
                throw new JwtAuthenticationException(TOKEN_NOT_FOUND.getMessage());
            }

            String accessToken = token.get();

            if (!jwtExtractor.validateToken(accessToken)) {
                request.setAttribute(JWT_ERROR, INVALID_TOKEN);
                log.info("토큰 실패");
                throw new JwtAuthenticationException(INVALID_TOKEN.getMessage());
            }

            if (jwtExtractor.isExpired(accessToken)) {
                request.setAttribute(JWT_ERROR, TOKEN_EXPIRED);
                throw new JwtAuthenticationException(TOKEN_EXPIRED.getMessage());
            }

            // authentication 없이 토큰에서 userId 추출
            UUID userId = UUID.fromString(jwtExtractor.getId(accessToken));

            redisUtil.deleteRefreshTokenByUserId(userId);
            log.info("리프레시 토큰 삭제 완료 for user id: {}", userId);

        } catch (JwtAuthenticationException e) {
            log.error("JWT 인증 예외 발생: {}", e.getMessage());
            // 필요하다면 여기서 response 상태코드 설정 가능
        } catch (Exception e) {
            log.error("로그아웃 처리 중 예외 발생: {}", e.getMessage(), e);
        }
    }


}
