package com.jiyoung.kikihi.platform.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryState {
    ORDER_RECEIVED("주문 접수"),
    PREPARING("상품 준비중"),
    PACKING("포장중"),
    SHIPPING("배송중"),
    DELIVERED("배송 완료");


    private final String description;
}
