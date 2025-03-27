package com.jiyoung.kikihi.platform.adapter.out.jpa.product.repository;

import com.jiyoung.kikihi.platform.adapter.out.jpa.product.ReactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionJpaRepository extends JpaRepository<ReactionJpaEntity, Long> {

    Optional<ReactionJpaEntity> findByProductIdAndUserId(Long productId, Long userId);

    @Modifying
    @Query("INSERT INTO ReactionJpaEntity (productId, userId) VALUES (:productId, :userId)")
    void saveByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM ReactionJpaEntity r WHERE r.productId = :productId AND r.userId = :userId")
    void deleteByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);
}
