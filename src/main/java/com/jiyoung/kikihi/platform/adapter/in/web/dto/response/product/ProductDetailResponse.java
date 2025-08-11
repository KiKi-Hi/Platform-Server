package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(
        name = "[응답][상품] 상품 상세 조회 Response",
        description = "상품 상세 정보를 반환하는 응답 DTO입니다."
)
public record ProductDetailResponse(
        @Schema(description = "상품 아이디", example = "101")
        String id,

        @Schema(description = "상품 썸네일 이미지 URL", example = "https://example.com/product/101.jpg")
        String thumbnail,

        @Schema(description = "제조사명", example = "독거미")
        String manufacturerName,

        @Schema(description = "카테고리명", example = "keyboard")
        String category,

        @Schema(description = "제품명", example = "독거미 Aula F99")
        String productName,

        @Schema(description = "할인율 (예: 0.15=15%)", example = "0.15")
        double discountRate,

        @Schema(description = "정상가(원)", example = "599000.0")
        double originalPrice,

        @Schema(description = "할인가(원)", example = "509000.0")
        double discountedPrice,

        @Schema(description = "북마크(좋아요)한 상품 여부", example = "true")
        boolean likedByMe,

        @Schema(description = "배송 정보 응답 DTO", implementation = DeliveryInfoResponse.class)
        DeliveryInfoResponse deliveryInfo,

        @Schema(description = "어울리는 추천 상품 목록", implementation = RecommendedItemResponse.class)
        List<RecommendedItemResponse> recommendedItems,

        @Schema(description = "상품 유의사항", example = "도착일은 배송지나 배송사 사정으로 변경 또는 지연될 수 있습니다.")
        String cautions,

        @Schema(description = "상세 정보 이미지 URL 목록", example = "[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]")
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

