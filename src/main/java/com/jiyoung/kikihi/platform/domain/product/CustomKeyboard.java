package com.jiyoung.kikihi.platform.domain.product;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.CustomRequest;
import com.jiyoung.kikihi.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CustomKeyboard extends BaseDomain {

    /*
        유저가 완성한 커스텀 키보드 관련 엔티티 이다.
     */

    private Long id;
    private Long frameId;
    private Long switchId;
    private Long keycapId;
    private String name;
    private double price;
    private String image_url;


    public static CustomKeyboard of(CustomRequest request) {
        return CustomKeyboard.builder()
                .frameId(request.getFrameId())
                .switchId(request.getSwitchId())
                .keycapId(request.getKeycapId())
                .name(request.getName())
                .price(request.getPrice())
                .image_url(request.getImage_url())
                .build();
    }

}
