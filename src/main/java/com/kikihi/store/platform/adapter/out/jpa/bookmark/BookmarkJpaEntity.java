package com.kikihi.store.platform.adapter.out.jpa.bookmark;

import com.kikihi.store.platform.adapter.out.jpa.BaseTimeEntity;
import com.kikihi.store.platform.domain.bookmark.Bookmark;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BookmarkJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    private String productId;

    private String category;

    /// 정적 팩토리 메서드
    public static BookmarkJpaEntity from(Bookmark bookmark) {
        return BookmarkJpaEntity.builder()
                .userId(bookmark.getUserId())
                .productId(bookmark.getProductId())
                .category(bookmark.getCategory())
                .build();
    }

    /// 정적 팩토리 메서드
    public Bookmark toDomain() {
        return Bookmark.builder()
                .id(id)
                .userId(userId)
                .productId(productId)
                .category(category)
                .build();
    }

}
