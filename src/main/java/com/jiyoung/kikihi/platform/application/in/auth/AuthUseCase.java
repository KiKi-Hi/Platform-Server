package com.jiyoung.kikihi.platform.application.in.auth;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthUseCase {
    /// 토큰 재발급 하기
    String reissue(String refreshToken, HttpServletResponse response);
}
