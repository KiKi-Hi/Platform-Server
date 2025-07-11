package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.Builder;

import java.util.List;

/**
 * 상품 상세 응답 DTO
 *
 * @param id                 상품 ID
 * @param manufacturerName   제조사명
 * @param category           카테고리
 * @param productName        제품명
 * @param discountRate       할인율 (예: 0.15는 15%)
 * @param originalPrice      최저가
 * @param discountedPrice    할인가
 * @param likedByMe          나의 북마크 여부
 * @param deliveryInfo       배송 정보 (배송비, 배송 종류, 예상 도착일 등)
 * @param recommendedItems   어울리는 상품 목록
 * @param cautions           상품 유의사항
 * @param imageUrl           상세 정보 이미지 URL 목록
 */

@Builder
public record ProductDetailResponse(
        String id,
        String manufacturerName,
        String category,
        String productName,
        double discountRate,
        double originalPrice,
        double discountedPrice,
        boolean likedByMe,
        DeliveryInfoResponse deliveryInfo,
        List<RecommendedItemResponse> recommendedItems,
        String cautions,
        List<String> imageUrl
) {

    public static ProductDetailResponse from(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .manufacturerName(product.getManufacturer())
                .category(product.getCategory())
                .productName(product.getName())
                .discountRate(0)
                .originalPrice(product.getPrice())
                .discountedPrice(product.getPrice())
                .likedByMe(false)
                .deliveryInfo(null)
                .recommendedItems(null)
                .cautions(null)
                .imageUrl(product.getAllDetailImages())
                .build();
    }

    public static ProductDetailResponse from(Product product, boolean likedByMe) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .manufacturerName(product.getManufacturer())
                .category(product.getCategory())
                .productName(product.getName())
                .discountRate(0)
                .originalPrice(product.getPrice())
                .discountedPrice(product.getPrice())
                .likedByMe(likedByMe)
                .deliveryInfo(null)
                .recommendedItems(null)
                .cautions(null)
                .imageUrl(product.getAllDetailImages())
                .build();
    }

}

