package com.jiyoung.kikihi.platform.application.in.auth;

import com.jiyoung.kikihi.security.oauth2.kakao.KaKaoDto;
import com.jiyoung.kikihi.platform.domain.user.User;

public interface AuthUseCase {
    User joinUser(KaKaoDto.KakaoUserInfoResponse userInfo);
}
