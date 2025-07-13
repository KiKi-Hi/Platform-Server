package com.jiyoung.kikihi.platform.adapter.out.jpa.order;

import com.jiyoung.kikihi.platform.adapter.out.jpa.user.AddressJpaEntity;
import com.jiyoung.kikihi.platform.domain.order.DeliveryInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delivery_info")
@AllArgsConstructor
@Builder
public class DeliveryInfoJpaEntity {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Embedded
    private AddressJpaEntity address;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }

    public static DeliveryInfoJpaEntity from(DeliveryInfo deliveryInfo) {
        return DeliveryInfoJpaEntity.builder()
                .address(AddressJpaEntity.from(deliveryInfo.getAddress()))
                .recipient(deliveryInfo.getRecipient())
                .phoneNumber(deliveryInfo.getPhoneNumber())
                .build();
    }
}
