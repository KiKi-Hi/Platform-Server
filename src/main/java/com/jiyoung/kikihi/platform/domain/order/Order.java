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
    private Double totalPrice;
    private OrderState state;


}
