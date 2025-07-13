package com.jiyoung.kikihi.platform.adapter.out.jpa.order;

import com.jiyoung.kikihi.platform.domain.order.Order;
import com.jiyoung.kikihi.platform.domain.order.OrderState;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@AllArgsConstructor
@Builder
public class OrderJpaEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "delivery_id", nullable = false)
    private Long deliveryId;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "state", nullable = false)
    private OrderState state;

    @Column (name = "total_price", nullable = false)
    private Double totalPrice = 0.0;

    public static OrderJpaEntity of(Order order) {
        return OrderJpaEntity.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .deliveryId(order.getDeliveryId())
                .paymentId(order.getPaymentId())
                .state(order.getState())
                .totalPrice(order.getTotalPrice())
                .build();
    }

}
