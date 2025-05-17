package com.jiyoung.kikihi.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationFilter.JWT_ERROR;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFailureHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorCode errorCode = (ErrorCode) request.getAttribute(JWT_ERROR);

        CustomException exception = new CustomException(errorCode);

        ApiResponse<Object> apiResponse = ApiResponse.fail(exception);

        // 응답 설정
        response.setStatus(apiResponse.httpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Jackson으로 ApiResponse를 JSON으로 직렬화
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
