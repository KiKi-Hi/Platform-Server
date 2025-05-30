package com.jiyoung.kikihi.platform.adapter.out.jpa.user;

import com.jiyoung.kikihi.platform.domain.user.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmail(String email);


    Optional<UserJpaEntity> findByProviderAndSocialId(Provider social, String socialId);

}
