package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product;


import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.Builder;
import java.util.List;

/**
 * 상품 목록 응답 DTO
 *
 * @param id                상품 아이디
 * @param thumbnail         썸네일 이미지 URL
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

}
