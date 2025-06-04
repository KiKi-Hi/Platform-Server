package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.platform.adapter.in.web.swagger.AuthControllerSpec;
import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.security.jwt.service.JwtTokenUseCase;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerSpec {

    private final JwtTokenUseCase tokenService;

    @PostMapping("/reissue")
    public ApiResponse<String> reissue(HttpServletRequest request, HttpServletResponse response) {

        /// 재발급 하기
        tokenService.reissueByRefreshToken(request, response);

        return ApiResponse.ok("성공적으로 재발급되었습니다.");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletRequest request, HttpServletResponse response) {

        // 유저
        UUID userId = principalDetails.getUser().getId();

        // 리프레쉬 토큰 삭제하기
        tokenService.logout(userId, request, response);

        return ApiResponse.ok("성공적으로 로그아웃되었습니다.");
    }

}
