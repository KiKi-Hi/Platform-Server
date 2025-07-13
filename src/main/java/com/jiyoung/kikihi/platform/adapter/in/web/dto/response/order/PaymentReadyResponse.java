package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.order;

import lombok.Builder;

import java.util.UUID;

/**
 * 주문 정보 저장 및 결제에 필요한 param 반환하는 DTO
 */

@Builder
public record PaymentReadyResponse(
        // 주문 ID
        UUID orderId,// 주문 번호
        double amount,// 결제 금액
        String successUrl,// 결제 성공 URL
        String failUrl // 결제 실패 URL
) {
    public static PaymentReadyResponse of(UUID orderId, double amount,String successUrl, String failUrl) {
        return PaymentReadyResponse.builder()
                .orderId(orderId)
                .amount(amount)
                .successUrl(successUrl)
                .failUrl(failUrl)
                .build();
    }
}
