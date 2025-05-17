package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.security.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.security.jwt.service.JWTService;
import com.jiyoung.kikihi.security.jwt.util.CookieUtil;
import com.jiyoung.kikihi.security.oauth2.domain.kakao.KaKaoUserInfo;
import com.jiyoung.kikihi.security.oauth2.service.dto.KaKaoDto;
import com.jiyoung.kikihi.security.oauth2.service.KakaoUtil;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.LoginDto;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.UserTokenDto;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.UserResponse;
import com.jiyoung.kikihi.platform.domain.user.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    /// 외부 요청
    private final KakaoUtil kakaoUtil;
//    private final GoogleUtil googleUtil;

    /// 저장
    private final UserService userService;

    /// 토큰 발급
    private final JWTService jwtService;
    private final CookieUtil cookieUtil;


    /**
     * 카카오 로그인 처리
     */

    public LoginDto kakaoLogin(String authCode, HttpServletResponse response) {

        // 1. 인가 코드 → access token → 사용자 정보 조회
        KaKaoDto.KakaoAccessToken accessToken = kakaoUtil.requestKakaoToken(authCode);
        KaKaoDto.KakaoUserInfoResponse userInfo = kakaoUtil.requestKakaoProfile(accessToken.access_token());

        log.debug("[Kakao Login] 액세스 토큰 수신 완료 - accessToken: {}", accessToken.access_token());

        Long kakaoId = userInfo.id();

        KaKaoUserInfo kaKaoUserInfo = new KaKaoUserInfo(userInfo);

        // 2. DB에서 회원 조회
        User user = userService.findByKakaoId(kakaoId);

        if (user == null) {
            log.info("[Kakao Login] 신규 회원, 회원가입 처리 - kakaoId: {}", kakaoId);
            user = userService.joinUser(kaKaoUserInfo);
        }

        JWTTokenDto jwtTokenDto = jwtService.generateJwtToken(UserTokenDto.from(user));// token정보
        setRefreshTokenCookie(jwtTokenDto.refreshToken(), response);

        UserResponse userResponse = UserResponse.from(user); // 유저 정보

        // 3. 정상 회원 → 정식 JWT 발급(카카오에서 받은 access token으로 유저정보를 활용해서 우리 서버 전용 토큰 발행)
        return LoginDto.of(
                jwtTokenDto, userResponse
        );
    }

    /**
     * 구글 로그인 처리
     */

    public LoginDto googleLogin(String authCode, HttpServletResponse response) {
        return null;
    }

    // 쿠키에 RefreshToken 설정 (HttpServletResponse 필요)
    public void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        cookieUtil.setCookie(refreshToken, response);
        log.info("🍪 쿠키에 RefreshToken 저장 완료 - key: {}", refreshToken);
    }


}
