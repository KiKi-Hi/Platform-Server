package com.jiyoung.kikihi.platform.domain.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Order {

    private UUID id;
    private UUID userId;
    private Long deliveryId;
    private Long paymentId;
    private Boolean state; // 가주문 테이블 (주문 완료)
    private Double totalPrice;

}
