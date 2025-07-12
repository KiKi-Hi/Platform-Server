package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product;

import lombok.Builder;

/**
 * 배송 정보 DTO
 *
 * @param shippingFee   배송비
 * @param shippingType  배송 종류 (예: 일반, 특급, 새벽 등)
 * @param estimatedDate 배송 예정일
 */

@Builder
record DeliveryInfoResponse(
        int shippingFee,
        String shippingType,
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
