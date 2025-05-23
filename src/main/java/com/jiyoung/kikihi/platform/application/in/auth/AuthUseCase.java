package com.jiyoung.kikihi.platform.application.in.auth;

import com.jiyoung.kikihi.security.oauth2.domain.OAuth2UserInfo;
import com.jiyoung.kikihi.platform.domain.user.User;

public interface AuthUseCase {

    User joinUser(OAuth2UserInfo userInfo);

    /// 토큰 재발급 하기

}
