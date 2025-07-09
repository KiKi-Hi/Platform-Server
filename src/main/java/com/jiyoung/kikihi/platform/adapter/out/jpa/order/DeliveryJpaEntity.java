package com.jiyoung.kikihi.platform.adapter.out.jpa.order;

import com.jiyoung.kikihi.platform.domain.order.DeliveryState;
import com.jiyoung.kikihi.platform.domain.user.Address;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries")
@AllArgsConstructor
@Builder
public class DeliveryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    // user의 address와 다름
    @Column(nullable = false)
    private String recipient;

    @Embedded
    private Address address;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(length = 500, nullable = true)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false)
    private DeliveryState deliveryState;


}
