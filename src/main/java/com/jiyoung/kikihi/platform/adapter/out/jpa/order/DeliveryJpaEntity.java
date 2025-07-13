package com.jiyoung.kikihi.platform.adapter.out.jpa.order;

import com.jiyoung.kikihi.platform.domain.order.DeliveryState;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries")
@AllArgsConstructor
@Builder
public class DeliveryJpaEntity {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "delivery_info_id", nullable = false)
    private UUID deliveryInfoId;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false)
    private DeliveryState deliveryState;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }

    public static DeliveryJpaEntity from(
            UUID orderId,
            UUID deliveryInfoId,
            String message,
            DeliveryState deliveryState) {
        return DeliveryJpaEntity.builder()
                .orderId(orderId)
                .deliveryInfoId(deliveryInfoId)
                .message(message)
                .deliveryState(deliveryState)
                .build();
    }
}
