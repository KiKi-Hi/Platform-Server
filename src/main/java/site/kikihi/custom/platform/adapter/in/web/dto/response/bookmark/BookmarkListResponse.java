package site.kikihi.custom.platform.adapter.in.web.dto.response.bookmark;

import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;
import site.kikihi.custom.platform.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.*;

/**
 * Bookmark 대한 결과 DTO
 * @param id
 * @param userId
 * @param products
 */

@Builder
@Schema(
        name = "[응답][북마크] 북마크 조회 Response",
        description = "사용자의 상품 북마크 정보를 반환하는 응답 DTO입니다."
)
public record BookmarkListResponse(
        @Schema(description = "북마크 ID", example = "88")
        Long id,

        @Schema(description = "사용자 UUID", example = "95ea60b2-f63b-434e-afc5-d5e5d6a505e7")
        UUID userId,

        @Schema(description = "북마크된 상품 정보", implementation = ProductListResponse.class)
        ProductListResponse products
) {

    /// 정적 팩토리 메서드
    public static BookmarkListResponse from(Bookmark bookmark, Product product) {
        return BookmarkListResponse.builder()
                .id(bookmark.getId())
                .userId(bookmark.getUserId())
                .products(ProductListResponse.from(product, true))
                .build();
    }
}
