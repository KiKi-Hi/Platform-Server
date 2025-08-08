package com.kikihi.store.platform.adapter.out.jpa.custom;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CustomKeyboardJpaRepository extends JpaRepository<CustomKeyboardJpaEntity, Long> {
    List<CustomKeyboardJpaEntity> findByIdIn(Set<Long> ids);

    Slice<CustomKeyboardJpaEntity> findByUserId(UUID userId);

    boolean existsByUserIdAndId(UUID userId, Long id);
}
