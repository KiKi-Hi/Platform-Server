package com.jiyoung.kikihi.platform.application.out.user;

import com.jiyoung.kikihi.platform.domain.user.User;

import java.util.Optional;

public interface UserPort {

    /// 저장
    User saveUser(User user);

    // 수정하기
    User updateUser(User user);

    /// 조회하기
    boolean checkExistingById(Long userId);

    Optional<User> loadUserById(Long userId);

}
