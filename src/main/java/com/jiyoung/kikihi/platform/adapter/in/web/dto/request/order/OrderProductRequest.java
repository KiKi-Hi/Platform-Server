package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order;

import lombok.Builder;

/**
 * 가주문 저장
 *
 * @param
 */

@Builder
public record OrderProductRequest(
        String productId,
        String productName,
        String optionName,
        int quantity,
        double price,
        int deliveryFee

) {
}
