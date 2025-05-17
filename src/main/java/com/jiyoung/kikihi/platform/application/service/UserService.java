package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.security.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.security.jwt.service.JWTService;
import com.jiyoung.kikihi.security.jwt.util.CookieUtil;
import com.jiyoung.kikihi.security.oauth2.domain.OAuth2UserInfo;
import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.application.in.auth.AuthUseCase;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.user.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements AuthUseCase {

    private final UserPort userPort;

    /// 외부 의존성
    private final JWTService jwtService;
    private final CookieUtil cookieUtil;

    // 회원가입
    @Override
    public User joinUser(OAuth2UserInfo userInfo) {

        /// 이메일 중복 체크
        boolean emailUsed = isEmailUsed(userInfo.getEmail());

        if (emailUsed) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.of(userInfo);
        return userPort.saveUser(user);
    }


    /// 재 로그인
    public String reissue(String refreshToken, HttpServletResponse response) {
        JWTTokenDto jwtTokenDto = jwtService.reissueJwtToken(refreshToken);

        setRefreshTokenCookie(jwtTokenDto.refreshToken(), response);

        return null;
    }


    /// 내부 함수
    private boolean isEmailUsed(String email) {
        return userPort.existsByEmail(email);
    }


    public User findByKakaoId(Long kakaoId) {
        return userPort.findByKakaoId(kakaoId);
    }

    // 쿠키에 RefreshToken 설정 (HttpServletResponse 필요)
    public void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        cookieUtil.setCookie(refreshToken, response);
        log.info("🍪 쿠키에 RefreshToken 저장 완료 - key: {}", refreshToken);
    }
}
