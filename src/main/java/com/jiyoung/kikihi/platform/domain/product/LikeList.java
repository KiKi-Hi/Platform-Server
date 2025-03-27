package com.jiyoung.kikihi.platform.domain.product;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikeList {
    private Long id;
    private Long userId;
    private Long productId;
}
