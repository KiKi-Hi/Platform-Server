package com.kikihi.store.platform.adapter.in.web.dto.response.bookmark;

import com.kikihi.store.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.kikihi.store.platform.domain.bookmark.Bookmark;
import com.kikihi.store.platform.domain.product.Product;
import lombok.Builder;

import java.util.*;

/**
 * Bookmark 대한 결과 DTO
 * @param id
 * @param userId
 * @param products
 */

@Builder
public record BookmarkResponse(
        Long id,
        UUID userId,
        ProductListResponse products
) {

    /// 정적 팩토리 메서드
    public static BookmarkResponse from(Bookmark bookmark, Product product) {
        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .userId(bookmark.getUserId())
                .products(ProductListResponse.from(product, true))
                .build();
    }
}
