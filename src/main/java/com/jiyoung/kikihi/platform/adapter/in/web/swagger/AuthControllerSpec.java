package com.jiyoung.kikihi.platform.adapter.in.web.swagger;

import com.jiyoung.kikihi.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.jiyoung.kikihi.security.jwt.util.JWTProvider.REFRESH_TOKEN_SUBJECT;

@Tag(name = "토큰 재발급 API", description = "유저의 토큰 재발급 API 입니다.")
public interface AuthControllerSpec {

    @Operation(
            summary = "RefeshToken을 바탕으로 AccessToken을 재발급 할 수 있습니다.",
            description = "쿠키가 존재하고, 카카오/구글을 통해 로그인한 회원에서 가능합니다."
    )
    ApiResponse<String> reissue(
            @RequestHeader(REFRESH_TOKEN_SUBJECT) String refreshToken,
            HttpServletResponse response);

}
