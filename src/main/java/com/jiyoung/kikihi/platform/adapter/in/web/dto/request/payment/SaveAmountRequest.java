package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.payment;

public record SaveAmountRequest(
    String paymentKey,
    String orderId,
    int amount
) {
}
