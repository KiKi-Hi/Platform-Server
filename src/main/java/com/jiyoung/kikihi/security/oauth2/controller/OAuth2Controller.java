package com.jiyoung.kikihi.security.oauth2.controller;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.LoginDto;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.UserResponse;
import com.jiyoung.kikihi.platform.application.service.AuthService;
import com.jiyoung.kikihi.security.oauth2.service.dto.KaKaoDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.jiyoung.kikihi.security.jwt.util.JWTProvider.ACCESS_TOKEN_SUBJECT;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final AuthService authService;

    /// 카카오
    // 임시로 authcode받는 로직 추가
    @GetMapping("/login/kakao")
    public ApiResponse<String> kakaoTMPLogin(@RequestParam("code") String authCode) {
        log.info("authCode" + authCode);
        return ApiResponse.ok(authCode);
    }

    // 카카오 로그인 -> 성공
    @PostMapping("/login/kakao")
    public ApiResponse<UserResponse> kakaoLogin(@RequestBody @Valid KaKaoDto.KakaoAuthCode kakaoAuthCode,
                                                HttpServletResponse response) {

        // authCode-> accessToken,refresh(카카오용) 토큰 가져오기 + 유저정보 받아오기
        LoginDto loginDto = authService.kakaoLogin(kakaoAuthCode.authCode(), response);

        addAccessTokenHeader(loginDto.jwtTokenDto().accessToken(), response);
        return ApiResponse.ok(loginDto.userResponse());

    }


    /// 구글
    @GetMapping("/login/google")
    public ApiResponse<String> googleTMPLogin(@RequestParam("code") String authCode) {
        log.info("authCode" + authCode);
        return ApiResponse.ok("String");
    }

    @PostMapping("/login/google")
    public ApiResponse<UserResponse> google(HttpServletResponse response) {
        return null;
    }

    private void addAccessTokenHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_SUBJECT, accessToken);
    }

}
