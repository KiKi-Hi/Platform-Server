package com.jiyoung.kikihi.security.oauth2.kakao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

/**
 * KakaoUtil은 카카오 OAuth2 인증 및 사용자 정보 요청을 담당하는 유틸 클래스
 */
@Slf4j
@Component
public class KakaoUtil {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    private final RestClient restClient = RestClient.create();

    /**
     *  인가 코드를 이용하여 카카오 토큰을 요청.
     */
    public KaKaoDto.KakaoAccessToken requestKakaoToken(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri); // 오타 수정
        params.add("code", authCode);

        return restClient.post()
                .uri(kakaoTokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(KaKaoDto.KakaoAccessToken.class);
    }

    /**
     * 액세스 토큰을 이용하여 카카오 사용자 정보를 요청
     */
    public KaKaoDto.KakaoUserInfoResponse requestKakaoProfile(String accessToken) {
        return restClient.get()
                .uri(kakaoUserInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KaKaoDto.KakaoUserInfoResponse.class);
    }
}
