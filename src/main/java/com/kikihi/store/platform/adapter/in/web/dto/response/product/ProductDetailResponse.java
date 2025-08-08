package com.kikihi.store.platform.adapter.in.web.dto.response.product;

import com.kikihi.store.platform.domain.product.Product;
import lombok.Builder;

import java.util.List;

/**
 * 상품 상세 응답 DTO
 *
 * @param id                 상품 ID
 * @param thumbnail          상품 썸네일
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
        String thumbnail,
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

    /// 정적 팩토리 메서드
    public static ProductDetailResponse from(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .manufacturerName(product.getManufacturer())
                .category(product.getCategory())
                .productName(product.getName())
                .discountRate(0)
                .originalPrice(product.getPrice())
                .discountedPrice(product.getPrice())
                .likedByMe(false)
                .deliveryInfo(DeliveryInfoResponse.of(3000, "일반배송", "3일 이내 발송 예정"))
                .recommendedItems(null)
                .cautions("도착일은 배송지나 배송사 사정으로 변경 또는 지연될 수 있습니다.")
                .imageUrl(product.getAllDetailImages())
                .build();
    }

    /// 정적 팩토리 메서드
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
                .deliveryInfo(DeliveryInfoResponse.of(3000, "일반배송", "3일 이내 발송 예정"))
                .recommendedItems(null)
                .cautions("도착일은 배송지나 배송사 사정으로 변경 또는 지연될 수 있습니다.")
                .imageUrl(product.getAllDetailImages())
                .build();
    }

}

