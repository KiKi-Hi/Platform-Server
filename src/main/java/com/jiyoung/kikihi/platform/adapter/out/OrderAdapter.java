package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.DeliveryInfoJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.OrderJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository.DeliveryInfoJpaRepository;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository.DeliveryJpaRepository;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository.OrderJpaRepository;
import com.jiyoung.kikihi.platform.application.out.order.DeliveryPort;
import com.jiyoung.kikihi.platform.application.out.order.OrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAdapter implements OrderPort, DeliveryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final DeliveryJpaRepository deliveryJpaRepository;
    private final DeliveryInfoJpaRepository deliveryInfoJpaRepository;


    @Override
    public void saveOrder(OrderJpaEntity order) {

    }

    @Override
    public void saveOrderToRedis(OrderJpaEntity order) {

    }

    @Override
    public void deleteOrderFromRedis(UUID orderId) {

    }

    @Override
    public void updateOrderStatus(UUID orderId, String status) {

    }

    @Override
    public List<OrderJpaEntity> findOrdersByUserId(UUID userId, Pageable pageable) {
        return List.of();
    }

    @Override
    public void expireOrder(UUID orderId) {

    }

    @Override
    public void publishOrderEvent(OrderJpaEntity order) {

    }


    @Override
    public UUID saveDeliveryInfo(DeliveryInfoJpaEntity deliveryInfo) {
        return deliveryInfoJpaRepository.save(deliveryInfo).getId();
    }
}

