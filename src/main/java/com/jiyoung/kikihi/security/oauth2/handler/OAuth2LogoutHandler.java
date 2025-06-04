package com.jiyoung.kikihi.security.oauth2.handler;

import com.jiyoung.kikihi.security.jwt.util.RedisUtil;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LogoutHandler extends SimpleUrlLogoutSuccessHandler {

    /*
        - 기존 레디스에서 리프레쉬 토큰 삭제
        - 세션 무효화
        - 쿠키 삭제
        - 시큐리티 컨텍스트에서 제거
        - 로그아웃 성공시, 프런트로 리다이렉트
     */
    @Value("${spring.front.host}")
    private String host;

    private final RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        /// 유저 정보 추출하기
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("onLogoutSuccess: {}", principal);

        UUID userId = principal.getUser().getId();
        log.info("로그 : userId = {}", userId);

        /// 리프레쉬토큰 레디스에서 삭제하기
        redisUtil.deleteRefreshTokenByUserId(userId);

        /// 시큐리티 컨텍스트 없애기
        SecurityContextHolder.getContext().setAuthentication(null);

        /// 리다이렉트 하기
        RedirectStrategy redirectStrategy = getRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, host + "/logout-success");
    }
}
