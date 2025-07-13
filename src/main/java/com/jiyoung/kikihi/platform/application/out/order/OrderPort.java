package com.jiyoung.kikihi.platform.application.out.order;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.OrderJpaEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * 주문을 생성하고 주문 정보를 조회하는 유스 케이스입니다.
 */
public interface OrderPort {

    // 주문 저장
    void saveOrder(OrderJpaEntity order);

    // redis에 주문 정보 넣기
    void saveOrderToRedis(OrderJpaEntity order);

    // redis에 있는 주문 항목 삭제
    void deleteOrderFromRedis(UUID orderId);

    // 주문 상태 변경 (진행/완료/실패)
    void updateOrderStatus(UUID orderId, String status);

    //주문 목록 조회 (페이징)
    List<OrderJpaEntity> findOrdersByUserId(UUID userId, Pageable pageable);

    //  주문 만료 처리
    void expireOrder(UUID orderId);

    // 주문 이벤트 발행
    void publishOrderEvent(OrderJpaEntity order);

}
