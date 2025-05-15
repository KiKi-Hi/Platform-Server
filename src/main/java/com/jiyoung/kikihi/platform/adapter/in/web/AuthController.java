package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.auth.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.global.auth.oauth2.kakao.KaKaoDto;
import com.jiyoung.kikihi.global.auth.oauth2.kakao.KakaoUtil;
import com.jiyoung.kikihi.global.common.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.LoginDto;
import com.jiyoung.kikihi.platform.application.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.jiyoung.kikihi.global.auth.jwt.util.JWTProvider.ACCESS_TOKEN_SUBJECT;
import static com.jiyoung.kikihi.global.auth.jwt.util.JWTProvider.REFRESH_TOKEN_SUBJECT;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoUtil kakaoUtil;

    @GetMapping("/login/kakao")
    public ApiResponse<String> kakaoTMPLogin(@RequestParam("code") String authCode) {
        log.info("authCode"+authCode);
        // authCode-> accessToken,refresh(카카오용) 토큰 가져오기 + 유저정보 받아오기
        LoginDto loginDto = authService.kakaoLogin(authCode);
        log.info(loginDto.jwtTokenDto().accessToken());
        return ApiResponse.ok(loginDto.jwtTokenDto().accessToken());

    }


    // 카카오 로그인
    @PostMapping("/login/kakao")
    public ApiResponse<LoginDto> kakaoLogin(@RequestBody @Valid KaKaoDto.KakaoAuthCode kakaoAuthCode, HttpServletResponse response) {

        // authCode-> accessToken,refresh(카카오용) 토큰 가져오기 + 유저정보 받아오기
        LoginDto loginDto = authService.kakaoLogin(kakaoAuthCode.authCode());
        responseToken(loginDto.jwtTokenDto(), response);

        return ApiResponse.ok(loginDto);
        
    }

    // 구글 로그인

    // refresh 토큰으로 accessToken, refreshToken을 재발급
    private void responseToken(JWTTokenDto jwtTokenDto, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_SUBJECT, jwtTokenDto.accessToken());
        response.setHeader(REFRESH_TOKEN_SUBJECT, jwtTokenDto.refreshToken());
    }

}
