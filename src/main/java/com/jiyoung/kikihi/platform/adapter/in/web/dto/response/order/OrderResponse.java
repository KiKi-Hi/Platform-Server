package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.order;

import lombok.Builder;

/**
 * 주문 정보 DTO
 *
 */

@Builder
public record OrderResponse(
        // 주문정보
        Long id,
        String orderNumber,
        String orderDate,
        String status,
        String totalPrice,
        // 배송 정보
        String deliveryAddress,
        String deliveryStatus,
        String deliveryTrackingNumber,
        // 결제 정보
        String paymentMethod,
        String paymentStatus,
        String paymentAmount

) {
}
