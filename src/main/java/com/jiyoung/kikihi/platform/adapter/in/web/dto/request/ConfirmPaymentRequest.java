package com.jiyoung.kikihi.platform.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ConfirmPaymentRequest(

        @NotBlank(message = "주문번호는 필수입니다.")
        String orderId,

        @NotBlank(message = "paymentKey는 필수입니다.")
        String paymentKey,

        @Min(value = 1, message = "결제 금액은 1원 이상이어야 합니다.")
        int amount
) {
}
