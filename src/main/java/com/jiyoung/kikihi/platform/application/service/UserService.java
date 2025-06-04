package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.security.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.security.jwt.service.JWTService;
import com.jiyoung.kikihi.security.jwt.util.CookieUtil;
import com.jiyoung.kikihi.platform.application.in.auth.AuthUseCase;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
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

    /// 재발급
    public String reissue(String refreshToken, HttpServletResponse response) {
        JWTTokenDto jwtTokenDto = jwtService.reissueJwtToken(refreshToken, response);

        return jwtTokenDto.accessToken();
    }


    /// 내부 함수
    private boolean isEmailUsed(String email) {
        return userPort.existsByEmail(email);
    }

}
