package com.jiyoung.kikihi.security.oauth2.domain.kakao;


import com.jiyoung.kikihi.security.oauth2.domain.OAuth2UserInfo;
import com.jiyoung.kikihi.security.oauth2.service.dto.KaKaoDto;

public class KaKaoUserInfo implements OAuth2UserInfo {

    private final KaKaoDto.KakaoUserInfoResponse userInfo;

    public KaKaoUserInfo(KaKaoDto.KakaoUserInfoResponse userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(userInfo.id());
    }

    @Override
    public String getEmail() {
        return userInfo.kakao_account().email();
    }

    @Override
    public String getUserName() {
        return userInfo.kakao_account().profile().nickname();
    }

    @Override
    public String getImageUrl() {
        return userInfo.kakao_account().profile().profileImage();
    }
}

