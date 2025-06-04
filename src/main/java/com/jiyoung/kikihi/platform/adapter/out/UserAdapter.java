package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.user.UserJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.user.UserJpaRepository;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.user.Provider;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User saveUser(User user) {
        var entity = UserJpaEntity.from(user);

        return userJpaRepository.save(entity)
                .toDomain();
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean checkExistingById(UUID userId) {
        return userJpaRepository.existsById(userId);
    }

    @Override
    public Optional<User> loadUserById(UUID userId) {
        return userJpaRepository.findById(userId)
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        Optional<UserJpaEntity> userJpaEntity = userJpaRepository.findByEmail(email);
        return userJpaEntity.isPresent();
    }

    @Override
    public Optional<User> loadUserBySocialAndSocialId(Provider social, String socialId) {
        return userJpaRepository.findByProviderAndSocialId(social, socialId)
                .map(UserJpaEntity::toDomain);
    }


}
