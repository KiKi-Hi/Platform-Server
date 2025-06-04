package com.jiyoung.kikihi.platform.adapter.in.web.swagger;

import com.jiyoung.kikihi.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "개발용 JWT 발급 API", description = "개발용 JWT 발급 API입니다.")
public interface DevAuthControllerSpec {

    @Operation(
            summary = "쿠키 발급 API",
            description = "개발의 편의를 위해 개발용 JWT를 즉시 발급 받을 수 있습니다."
    )
    ApiResponse<String> devLogin(HttpServletResponse response);

}
