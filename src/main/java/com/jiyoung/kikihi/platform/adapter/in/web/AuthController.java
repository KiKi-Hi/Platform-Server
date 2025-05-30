package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.platform.adapter.in.web.swagger.AuthControllerSpec;
import com.jiyoung.kikihi.platform.application.service.UserService;
import com.jiyoung.kikihi.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.jiyoung.kikihi.security.jwt.util.JWTProvider.ACCESS_TOKEN_SUBJECT;
import static com.jiyoung.kikihi.security.jwt.util.JWTProvider.REFRESH_TOKEN_SUBJECT;

@Slf4j
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerSpec {

    private final UserService userService;

    // refresh 토큰으로 accessToken, refreshToken을 재발급
    @PostMapping("/reissue")
    public ApiResponse<String> reissue(@RequestHeader(REFRESH_TOKEN_SUBJECT) String refreshToken, HttpServletResponse response) {
        String accessToken = userService.reissue(refreshToken, response);
        addAccessTokenHeader(accessToken, response);
        return ApiResponse.ok("성공적으로 재발급되었습니다.");
    }

    private void addAccessTokenHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_SUBJECT, accessToken);
    }


}
