package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.user.UserJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.user.UserJpaRepository;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User saveUser(User user) {
        var entity=UserJpaEntity.from(user);
        UserJpaEntity savedEntity =userJpaRepository.save(entity);
        return UserJpaEntity.toDomain(savedEntity);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean checkExistingById(Long userId) {
        return false;
    }

    @Override
    public Optional<User> loadUserById(Long userId) {
        return Optional.empty();
    }

    @Override
    public User findByKakaoId(Long kakaoId) {
        return null;
    }


}
