package com.jiyoung.kikihi.platform.domain.product;

import com.jiyoung.kikihi.platform.domain.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Custom extends BaseDomain {

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

}
