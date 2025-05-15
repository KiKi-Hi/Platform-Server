package com.jiyoung.kikihi.global.auth.jwt.dto;


import lombok.Builder;

@Builder
public record JWTTokenDto(
        String accessToken,
        String refreshToken
) {
    public static JWTTokenDto of(String accessToken, String refreshToken) {
        return new JWTTokenDto(accessToken, refreshToken);
    }
}
