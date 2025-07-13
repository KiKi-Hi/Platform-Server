package com.jiyoung.kikihi.platform.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderState {

    PENDING("주문 대기"),
    PROCESSING("주문 처리 중"),
    COMPLETED("주문 완료"),
    CANCELLED("주문 취소"),
    REFUNDED("환불 완료"),
    FAILED("주문 실패");


    private final String description;
}
