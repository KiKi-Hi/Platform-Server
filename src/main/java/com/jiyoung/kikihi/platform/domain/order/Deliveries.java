package com.jiyoung.kikihi.platform.domain.order;

import com.jiyoung.kikihi.platform.domain.user.Address;
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
public class Deliveries {
    private Long id;
    private UUID orderId;
    private DeliveryInfo deliveryInfo;
    private String message;
    private DeliveryState state; // 배송 상태 (예: 준비중, 배송중, 배송완료 등)

}
