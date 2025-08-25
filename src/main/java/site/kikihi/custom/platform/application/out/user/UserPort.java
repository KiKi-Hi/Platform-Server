package site.kikihi.custom.platform.application.out.user;

import site.kikihi.custom.platform.domain.user.Provider;
import site.kikihi.custom.platform.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPort {

    /// 저장
    User saveUser(User user);

    // 수정하기
    void updateUser(User user);

    /// 조회하기
    boolean checkExistingById(UUID userId);

    Optional<User> loadUserById(UUID userId);

    boolean existsByEmail(String email);

    Optional<User> loadUserBySocialAndSocialId(Provider social, String socialId);

}
