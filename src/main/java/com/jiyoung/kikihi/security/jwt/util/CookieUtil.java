package com.jiyoung.kikihi.security.jwt.util;

import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieUtil {

    @Value("${kikihi.jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${kikihi.jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    @Value("${kikihi.auth.jwt.secureOption}")
    private boolean secureOption;

    @Value("${kikihi.auth.jwt.sameSiteOption}")
    private String sameSiteOption;

    @Value("${kikihi.auth.jwt.cookiePathOption}")
    private String cookiePathOption;

    // 쿠키 설정
    public void setAccessCookie(String accessToken, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                .maxAge(accessTokenExpiration)
                .path(cookiePathOption)
                .httpOnly(true)
                .secure(secureOption) // Dev에서는 false, Prod에서는 true
                .sameSite(sameSiteOption)
                .build();

        log.info("set access cookie: {}", cookie);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // 쿠키 설정
    public void setRefreshCookie(String refreshToken, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .maxAge(refreshTokenExpiration)
                .path(cookiePathOption)
                .httpOnly(true)
                .secure(secureOption) //https 적용 시 true
                .sameSite(sameSiteOption)
                .build();

        log.info("set refresh cookie: {}", cookie);

        response.addHeader("Set-Cookie", cookie.toString());
    }

    // 쿠키 가져오기
    private static Cookie[] getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND_IN_COOKIE);
        }
        return cookies;
    }

    public void deleteAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", "")
                .maxAge(0)
                .path(cookiePathOption)
                .secure(secureOption)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("REFRESH_TOKEN", "")
                .maxAge(0)
                .path(cookiePathOption)
                .secure(secureOption)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


}
