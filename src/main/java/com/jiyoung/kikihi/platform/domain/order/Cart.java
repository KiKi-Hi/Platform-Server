package com.jiyoung.kikihi.platform.domain.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/*
* 각 상품에 대한 주문 정보를 담는 클래스입니다.
*
* */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Cart {
    private Long id;
    private UUID userId;
    private UUID productId;
    private Integer quantity;
}
