package com.jiyoung.kikihi.platform.adapter.out.jpa.custom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CustomKeyboardJpaRepository extends JpaRepository<CustomKeyboardJpaEntity, Long> {
    List<CustomKeyboardJpaEntity> findByIdIn(Set<Long> ids);
}
