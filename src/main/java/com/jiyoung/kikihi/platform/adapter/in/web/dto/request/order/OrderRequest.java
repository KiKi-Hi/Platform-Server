package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order;

import lombok.Builder;

import java.util.Collection;
import java.util.List;

/**
 * 가주문 저장
 *
 * @param
 */

@Builder
public record OrderRequest(

        DeliveryInfoRequest deliveryInfoRequest,
        List<OrderProductRequest> orderProductList,  // 주문하려고 하는 상품 목록 & 수량 리스트

        int totalPrice,
        String orderName,
        String idempotencyKey    // 중복 주문 방지용

) {

}
