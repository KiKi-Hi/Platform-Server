package com.jiyoung.kikihi.platform.adapter.out.jpa.user;

import com.jiyoung.kikihi.platform.domain.user.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findById(UUID id);


    Optional<UserJpaEntity> findByProviderAndSocialId(Provider social, String socialId);

}
