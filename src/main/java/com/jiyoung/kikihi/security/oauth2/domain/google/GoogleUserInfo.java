package com.jiyoung.kikihi.security.oauth2.domain.google;

import com.jiyoung.kikihi.security.oauth2.domain.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes; // getAttributes()

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "GOOGLE";  // 소문자로 통일 추천
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getUserName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getImageUrl() {
        return attributes.get("picture").toString();
    }
}
