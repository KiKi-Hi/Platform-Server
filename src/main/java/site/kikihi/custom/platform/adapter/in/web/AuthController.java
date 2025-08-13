package site.kikihi.custom.platform.adapter.in.web;

import site.kikihi.custom.platform.adapter.in.web.swagger.AuthControllerSpec;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.security.jwt.service.JwtTokenUseCase;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
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
    public ApiResponse<Void> reissue(HttpServletRequest request, HttpServletResponse response) {

        /// 재발급 하기
        tokenService.reissueByRefreshToken(request, response);

        return ApiResponse.created();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletRequest request, HttpServletResponse response) {

        // 유저
        UUID userId = principalDetails.getUser().getId();

        // 리프레쉬 토큰 삭제하기
        tokenService.logout(userId, request, response);

        return ApiResponse.deleted();
    }
}
