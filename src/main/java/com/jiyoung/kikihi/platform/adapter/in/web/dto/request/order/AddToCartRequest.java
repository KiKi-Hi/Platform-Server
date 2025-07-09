package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order;

import lombok.Builder;

/**
 * 장바구니 저장 DTO
 *
 * @param productId  상품 ID
 * @param quantity  상품 수령
 */

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Builder
public record AddToCartRequest(
        @NotBlank(message = "상품 ID는 필수입니다.")
        String productId,

        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        int quantity
) {
}
