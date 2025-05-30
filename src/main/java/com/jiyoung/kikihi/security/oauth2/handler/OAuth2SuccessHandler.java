package com.jiyoung.kikihi.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.UserTokenDto;
import com.jiyoung.kikihi.platform.domain.user.User;
import com.jiyoung.kikihi.security.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.security.jwt.service.JWTService;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTService tokenService;
    private final ObjectMapper objectMapper;

    /*
        기존에 존재하는 유저의 경우, 토큰 발급을 진행합니다.
     */

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        User user = principal.getUser();
        UserTokenDto userTokenDto = UserTokenDto.from(user);

        /// AccessToken과 Refresh 토큰 생성
        JWTTokenDto token = tokenService.generateJwtToken(userTokenDto);

        // ApiResponse에 담을 데이터 (Map 혹은 DTO)

        ApiResponse<JWTTokenDto> apiResponse = ApiResponse.ok(token);

        // 응답 설정
        response.setStatus(apiResponse.httpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Jackson으로 ApiResponse를 JSON으로 직렬화
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
