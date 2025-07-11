package com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkJpaEntity, Long> {

    List<BookmarkJpaEntity> findByUserIdAndCategory(UUID userId, String category);

    boolean existsByUserIdAndId(UUID userId, Long id);


    boolean existsByProductIdAndUserId(String productId, UUID userId);
}
