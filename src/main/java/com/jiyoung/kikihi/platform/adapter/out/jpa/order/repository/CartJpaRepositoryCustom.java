package com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartJpaRepositoryCustom {
    List<String> findProductIdsByUserId(UUID userId);

    void deleteByProductId(String productId);

    Optional<Object> findByProductIdAndUserId(String productId, UUID userId);
}
