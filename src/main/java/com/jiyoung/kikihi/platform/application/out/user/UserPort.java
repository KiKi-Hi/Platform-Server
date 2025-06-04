package com.jiyoung.kikihi.platform.application.out.user;

import com.jiyoung.kikihi.platform.domain.user.Provider;
import com.jiyoung.kikihi.platform.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPort {

    /// 저장
    User saveUser(User user);

    // 수정하기
    User updateUser(User user);

    /// 조회하기
    boolean checkExistingById(Long userId);

    Optional<User> loadUserById(UUID userId);

    boolean existsByEmail(String email);

    Optional<User> loadUserBySocialAndSocialId(Provider social, String socialId);

}
