package com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkJpaEntity, Long> {

    List<BookmarkJpaEntity> findByUserIdAndCategory(UUID userId, String category);

    boolean existsByUserIdAndId(UUID userId, Long id);

    boolean existsByProductIdAndUserId(String productId, UUID userId);

    boolean existsByUserIdAndProductId(UUID userId, String productId);

    @Query("select b.productId, count(b) from BookmarkJpaEntity b group by b.productId order by count(b) desc")
    List<Object[]> findProductIdAndBookmarkCountOrderByCountDesc();

}
