package com.jiyoung.kikihi.platform.adapter.in.web.dto;

import com.jiyoung.kikihi.security.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.UserResponse;
import lombok.Builder;

@Builder
public record LoginDto(
        JWTTokenDto jwtTokenDto,
        UserResponse userResponse
) {
    public static LoginDto of(JWTTokenDto jwtTokenDto, UserResponse userResponse) {
        return LoginDto.builder()
                .jwtTokenDto(jwtTokenDto)
                .userResponse(userResponse)
                .build();
    }

    public static LoginDto from(JWTTokenDto jwtTokenDto) {
        return LoginDto.builder()
                .jwtTokenDto(jwtTokenDto)
                .build();
    }

}
