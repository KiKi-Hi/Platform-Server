package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.custom;

import com.jiyoung.kikihi.platform.domain.custom.CustomKeyboardWithName;
import lombok.Builder;

/**
 * 커스텀 키보드 목록 조회 DTO
 * @param customId              커스텀 ID
 * @param customName            커스텀 이름
 * @param customKeyboardType    커스텀 타입
 * @param housingName           하우징 이름
 * @param keyCapName            키캡 이름
 * @param switchName            스위치 이름
 * @param thumbnail             썸네일
 * @param price                 가격
 */
@Builder
public record CustomKeyboardDetailResponse(
        Long customId,
        String customName,
        String customKeyboardType,
        String housingName,
        String keyCapName,
        String switchName,
        String thumbnail,
        double price
) {

    /// 정적 팩토리 메서드
    public static CustomKeyboardDetailResponse from(CustomKeyboardWithName entity){
        return CustomKeyboardDetailResponse.builder()
                .customId(entity.id())
                .customName(entity.name())
                .customKeyboardType(entity.layout())
                .housingName(entity.frameName())
                .keyCapName(entity.keyCapName())
                .switchName(entity.switchName())
                .thumbnail(entity.imageUrl())
                .price(entity.totalPrice())
                .build();
    }

}
