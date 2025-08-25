package site.kikihi.custom.platform.adapter.out;

import org.springframework.transaction.annotation.Transactional;
import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.out.jpa.user.UserJpaEntity;
import site.kikihi.custom.platform.adapter.out.jpa.user.UserJpaRepository;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.user.Provider;
import site.kikihi.custom.platform.domain.user.User;
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
    @Transactional
    public void updateUser(User user) {

        /// 조회
        var entity = userJpaRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.USER_NOT_FOUND.getMessage()));

        /// 자동저장 여부 수정
        entity.updateSearch(user.isSearch());
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
