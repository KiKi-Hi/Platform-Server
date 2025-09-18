package site.kikihi.custom.platform.adapter.in.web.dto.response.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import site.kikihi.custom.platform.domain.product.Product;

import java.util.List;

/**
 * 상품 상세 응답 DTO
 *
 * @param id                 상품 ID
 * @param thumbnail          상품 썸네일
 * @param manufacturerName   제조사명
 * @param productName        제품명
 * @param price
 * @param likedByMe          나의 북마크 여부
 */

@Builder
@Schema(
        name = "[응답][튜토리얼] 추천 상품 응답 Response",
        description = "튜토리얼 키보드 추천 리스트 응답"
)
public record KeyboardRecommendationResponse(

        @Schema(description = "상품 아이디", example = "101")
        String id,

        @Schema(description = "상품 썸네일 이미지 URL", example = "https://example.com/product/101.jpg")
        String thumbnail,

        @Schema(description = "제조사명", example = "독거미")
        String manufacturerName,

        @Schema(description = "제품명", example = "독거미 Aula F99")
        String productName,

        @Schema(description = "정상가(원)", example = "599000.0")
        Double price,

        @Schema(description = "북마크(좋아요)한 상품 여부", example = "true")
        boolean likedByMe

) {

    /// 정적 팩토리 메서드
    // 단일 객체 변환 메서드 추가
    public static KeyboardRecommendationResponse from(Product product, boolean likedByMe) {
        return KeyboardRecommendationResponse.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .manufacturerName(product.getManufacturer())
                .productName(product.getName())
                .price(product.getPrice())
                .likedByMe(likedByMe)
                .build();
    }


}

