package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product;


import com.jiyoung.kikihi.platform.domain.product.Product;
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
public record ProductListResponse(
        String id,
        String thumbnail,
        String category,
        String manufacturerName,
        String productName,
        double discountRate,
        double discountedPrice,
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
