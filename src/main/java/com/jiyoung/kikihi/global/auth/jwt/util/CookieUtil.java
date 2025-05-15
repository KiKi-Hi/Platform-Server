package com.jiyoung.kikihi.global.auth.jwt.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.UUID;

public class CookieUtil {
    
    @Value("${kikihi.auth.jwt.cookieMaxAge}")
    private Long cookieMaxAge;

    @Value("${kikihi.auth.jwt.secureOption}")
    private boolean secureOption;

    @Value("${kikihi.auth.jwt.cookiePathOption}")
    private String cookiePathOption;

    // 쿠키 설정
    public void setCookie(String userId, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(userId)
                .maxAge(cookieMaxAge)
                .path(cookiePathOption)
                .secure(secureOption) //https 적용 시 true
                .httpOnly(true)
                .sameSite("None")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }
    // 쿠키 삭제
    public void deleteCookie(HttpServletResponse response, UUID userId) {
        ResponseCookie cookie = ResponseCookie.from(String.valueOf(userId), "value")
                .maxAge(0) // 즉시만료
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
