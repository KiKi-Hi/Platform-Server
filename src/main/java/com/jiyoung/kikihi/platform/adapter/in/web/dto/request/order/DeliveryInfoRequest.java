package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order;

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
        String recipient,
        String postCode,
        String address,
        String detailedAddress,
        String phoneNumber
) {
}
