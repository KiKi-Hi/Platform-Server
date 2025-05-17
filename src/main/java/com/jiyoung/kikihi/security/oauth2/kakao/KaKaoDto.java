package com.jiyoung.kikihi.security.oauth2.kakao;


import jakarta.validation.constraints.NotBlank;

public class KaKaoDto {

    public record KakaoAuthCode(
            @NotBlank String authCode
    ){}

    public record KakaoAccessToken(
            String access_token,
            String token_type,
            String refresh_token,
            int expires_in,
            String scope,
            int refresh_token_expires_in
    ) {}

    public record KakaoUserInfoResponse(
            Long id,
            KakaoAccount kakao_account
    ) {}

    public record KakaoAccount(
            Boolean is_email_valid,// 유효한 형식인지
            Boolean is_email_verified,// 사용자에 의해 이메일이 인증됐는지
            String email,
            Profile profile
    ) {}

    public record Profile(
            String nickname,
            String profileImage,
            Boolean is_default_nickname
    ) {}
}
