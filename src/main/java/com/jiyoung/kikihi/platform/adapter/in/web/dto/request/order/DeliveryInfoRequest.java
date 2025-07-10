package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * 장바구니 저장 DTO
 *
 * @param recipient 받는 사람
 * @param postCode 우편번호
 * @param address 주소
 * @param detailedAddress 상세 주소
 * @param phoneNumber 전화번호
 *
 */

@Builder
public record DeliveryInfoRequest(
        @NotBlank(message = "받는 사람 이름은 필수입니다.")
        String recipient,

        @NotBlank(message = "우편번호는 필수입니다.")
        @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다.")
        String postCode,

        @NotBlank(message = "주소는 필수입니다.")
        String address,

        @NotBlank(message = "상세 주소는 필수입니다.")
        String detailedAddress,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(
                regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10,11}$",
                message = "전화번호 형식이 올바르지 않습니다."
        )
        String phoneNumber
) {
}
