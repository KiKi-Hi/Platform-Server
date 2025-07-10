package com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.CartJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartJpaRepositoryCustom {
    List<String> findProductIdsByUserId(UUID userId);

    void deleteByProductId(String productId,UUID userId);

    Optional<CartJpaEntity> findByProductIdAndUserId(String productId, UUID userId);
}
