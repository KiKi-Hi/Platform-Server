package com.jiyoung.kikihi.platform.adapter.in.web.swagger;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@Tag(name = "인증 관련 API", description = "유저의 토큰 재발급, 로그아웃의 기능을 수행하는 API 입니다.")
public interface AuthControllerSpec {

    @Operation(
            summary = "토큰 재발급 API",
            description = "RefeshToken을 바탕으로 AccessToken을 재발급 할 수 있습니다."
    )
    ApiResponse<String> reissue(
            HttpServletRequest request,
            HttpServletResponse response);

    @Operation(
            summary = "로그아웃 API",
            description = "쿠키가 존재하고, 카카오/구글을 통해 로그인한 회원에서 가능합니다."
    )
    ApiResponse<String> logout(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            HttpServletRequest request,
            HttpServletResponse response);

}
