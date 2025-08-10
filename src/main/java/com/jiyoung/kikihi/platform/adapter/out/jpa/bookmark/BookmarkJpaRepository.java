package com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark;

import com.jiyoung.kikihi.platform.adapter.out.jpa.bookmark.projection.ProductIdWithCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkJpaEntity, Long> {

    List<BookmarkJpaEntity> findByUserIdAndCategory(UUID userId, String category);

    boolean existsByUserIdAndId(UUID userId, Long id);

    boolean existsByProductIdAndUserId(String productId, UUID userId);

    boolean existsByUserIdAndProductId(UUID userId, String productId);

    /**
     * 북마크 상위 인기 개수 조회
     * @param pageable  페이징
     */
    @Query("select b.productId as productId , count(b) as count " +
            "from BookmarkJpaEntity b " +
            "group by b.productId " +
            "order by count(b) desc ")
    List<ProductIdWithCount> findBookmarkAndCount(Pageable pageable);


}
