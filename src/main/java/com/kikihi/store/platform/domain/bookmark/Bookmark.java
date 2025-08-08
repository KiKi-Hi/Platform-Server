package com.kikihi.store.platform.domain.bookmark;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Bookmark {

    private Long id;

    private String productId;

    private UUID userId;

    private String category;

    /// 정적 팩토리 메서드
    public static Bookmark of(String productId, UUID userId, String category) {
        return Bookmark.builder()
                .productId(productId)
                .userId(userId)
                .category(category)
                .build();
    }

}
