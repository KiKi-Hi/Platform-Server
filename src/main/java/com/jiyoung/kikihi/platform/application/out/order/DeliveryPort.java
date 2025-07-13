package com.jiyoung.kikihi.platform.application.out.order;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.DeliveryInfoJpaEntity;

import java.util.UUID;

/**
 * 주문을 생성하고 주문 정보를 조회하는 유스 케이스입니다.
 */
public interface DeliveryPort {

    // 배달 정보 저장
    UUID saveDeliveryInfo(DeliveryInfoJpaEntity deliveryInfoJpaEntity);



}
