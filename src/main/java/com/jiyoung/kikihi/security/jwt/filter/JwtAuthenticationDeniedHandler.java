package com.jiyoung.kikihi.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {


        /// 응답 내용 생성
        ApiResponse<Object> fail = ApiResponse.fail(
                new CustomException(ErrorCode.FORBIDDEN, accessDeniedException.getMessage())
        );


        /// response 제작
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        objectMapper.writeValue(response.getWriter(), fail);

    }
}
