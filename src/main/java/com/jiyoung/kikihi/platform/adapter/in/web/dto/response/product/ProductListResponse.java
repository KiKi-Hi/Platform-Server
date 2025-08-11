package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product;


import com.jiyoung.kikihi.platform.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.util.List;
import java.util.Set;

/**
 * 상품 목록 응답 DTO
 *
 * @param id                상품 아이디
 * @param thumbnail         썸네일 이미지 URL
 * @param category          카테고리
 * @param manufacturerName  제조사명
 * @param productName       제품명
 * @param discountRate      할인율 (예: 0.2 = 20%)
 * @param discountedPrice   할인가 (정상가에서 할인 적용된 가격)
 * @param likedByMe         나의 좋아요 여부 (true/false)
 */

@Builder
@Schema(name = "[응답][상품] 상품 목록 조회 Response", description = "상품 목록 조회를 위한 DTO입니다.")
public record ProductListResponse(

        @Schema(description = "상품 아이디", example = "101")
        String id,

        @Schema(description = "상품 썸네일 이미지 URL", example = "https://example.com/product/101.jpg")
        String thumbnail,

        @Schema(description = "카테고리명", example = "keycap")
        String category,

        @Schema(description = "제조사명", example = "애플")
        String manufacturerName,

        @Schema(description = "제품명", example = "맥북 키보드")
        String productName,

        @Schema(description = "할인율 (0.2 = 20%)", example = "0.15")
        double discountRate,

        @Schema(description = "할인가 (정상가에서 할인 적용된 가격)", example = "339000.0")
        double discountedPrice,

        @Schema(description = "내가 좋아요(북마크)한 상품 여부", example = "true")
        boolean likedByMe
) {

    /// 정적 팩토리 메서드
    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .category(product.getCategory())
                .manufacturerName(product.getManufacturer())
                .productName(product.getName())
                .discountRate(0)
                .discountedPrice(product.getPrice())
                .likedByMe(false)
                .build();
    }

    /// 정적 팩토리 메서드
    public static List<ProductListResponse> from(List<Product> products) {
        return products.stream()
                .map(ProductListResponse::from)
                .toList();
    }

    /// 북마크한 내용이 있을 때, 정적 팩토리 메서드
    public static List<ProductListResponse> from(List<Product> products, Set<String> bookmarkProductIds) {
        return products.stream()
                .map(product -> ProductListResponse.from(
                        product,
                        bookmarkProductIds.contains(product.getId())))
                .toList();
    }

    /// 북마크한 내용이 있을 때 사용하는, 내부 정적 팩토리 메서드
    public static ProductListResponse from(Product product, boolean likedByMe) {
        return ProductListResponse.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .category(product.getCategory())
                .manufacturerName(product.getManufacturer())
                .productName(product.getName())
                .discountRate(0)
                .discountedPrice(product.getPrice())
                .likedByMe(likedByMe)
                .build();
    }
}
