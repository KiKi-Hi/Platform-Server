package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.auth.oauth2.kakao.KaKaoDto;
import com.jiyoung.kikihi.global.auth.oauth2.kakao.KakaoUtil;
import com.jiyoung.kikihi.global.common.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.LoginDto;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.UserResponse;
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


    // 임시로 authcode받는 로직 추가
    @GetMapping("/login/kakao")
    public ApiResponse<String> kakaoTMPLogin(@RequestParam("code") String authCode) {
        log.info("authCode" + authCode);
        return ApiResponse.ok(authCode);

    }

    // 카카오 로그인 -> 성공
    @PostMapping("/login/kakao")
    public ApiResponse<UserResponse> kakaoLogin(@RequestBody @Valid KaKaoDto.KakaoAuthCode kakaoAuthCode, HttpServletResponse response) {
        // authCode-> accessToken,refresh(카카오용) 토큰 가져오기 + 유저정보 받아오기
        LoginDto loginDto = authService.kakaoLogin(kakaoAuthCode.authCode(), response);
        addAccessTokenHeader(loginDto.jwtTokenDto().accessToken(), response);
        return ApiResponse.ok(loginDto.userResponse());

    }

    // refresh 토큰으로 accessToken, refreshToken을 재발급
    @PostMapping("/reissue")
    public ApiResponse<String> reissue(@RequestHeader(REFRESH_TOKEN_SUBJECT) String refreshToken, HttpServletResponse response) {
        String accessToken = authService.reissue(refreshToken, response);
        addAccessTokenHeader(accessToken, response);
        return ApiResponse.ok("성공적으로 재발급되었습니다.");
    }

    private void addAccessTokenHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_SUBJECT, accessToken);
    }


}
