package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.UserTokenDto;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.DevAuthControllerSpec;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.user.Address;
import com.jiyoung.kikihi.platform.domain.user.Provider;
import com.jiyoung.kikihi.platform.domain.user.Role;
import com.jiyoung.kikihi.platform.domain.user.User;
import com.jiyoung.kikihi.security.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.security.jwt.service.JWTService;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Profile("prod")  // prod 환경에서만 활성화
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class DevAuthController implements DevAuthControllerSpec {

    private final UserPort userPort;
    private final JWTService tokenProvider;

    @PostMapping("/dev-login")
    public ApiResponse<JWTTokenDto> devLogin() {
        /// 테스트용 사용자 정보 생성
        User dev = createDev();

        User user = userPort.saveUser(dev);

        /// PrincipalDetails 생성 (시스템에 따라 다름)
        PrincipalDetails principalDetails = PrincipalDetails.of(user);

        /// Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, null, principalDetails.getAuthorities()
        );

        /// SecurityContextHolder 에 인증 정보 등록
         SecurityContextHolder.getContext().setAuthentication(authentication);

        /// JWT 토큰 생성
        UserTokenDto userTokenDto = UserTokenDto.from(user);

        JWTTokenDto response = tokenProvider.generateJwtToken(userTokenDto);

        return ApiResponse.ok(response);
    }

    /// 임시 유저 생성
    private User createDev() {
        return User.builder()
                .id(UUID.randomUUID())
                .socialId("dev-kakao-id")
                .email("zipte_kakao@example.com")
                .profileImage("http://image-url")
                .phoneNumber("010-1111-1111")
                .name("kakao개발자")
                .provider(Provider.KAKAO) // 테스트용 값 (Enum)
                .role(Role.ADMIN) // 관리자 권한 부여
                .address(Address.of())
                .build();
    }

}

