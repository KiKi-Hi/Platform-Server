package site.kikihi.custom.platform.adapter.in.web.dto.response.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 배송 정보 DTO
 *
 * @param shippingFee   배송비
 * @param shippingType  배송 종류 (예: 일반, 특급, 새벽 등)
 * @param estimatedDate 배송 예정일
 */

@Builder
@Schema(
        name = "[응답][상품] 배송 정보 Response",
        description = "상품의 배송 정보(배송비, 배송 종류, 예정일 등)를 담는 응답 DTO입니다."
)
public record DeliveryInfoResponse(
        @Schema(description = "배송비(원)", example = "3000")
        int shippingFee,

        @Schema(description = "배송 종류", example = "일반배송")
        String shippingType,

        @Schema(description = "배송 예정일", example = "3일 이내 발송 예정")
        String estimatedDate
) {
    public static DeliveryInfoResponse of(int shippingFee, String shippingType, String estimatedDate) {
        return DeliveryInfoResponse.builder()
                .shippingFee(shippingFee)
                .shippingType(shippingType)
                .estimatedDate(estimatedDate)
                .build();
    }

}
