package com.jiyoung.kikihi.platform.adapter.out.jpa.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/*
 * 각 상품에 대한 주문 정보를 담는 클래스입니다.
 *
 * */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "carts")
@AllArgsConstructor
@Builder
public class CartJpaEntity {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private String productId;

    private Integer quantity;

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public static CartJpaEntity from(String productId, UUID userId, Integer quantity) {
        return CartJpaEntity.builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .userId(userId)
                .quantity(quantity)
                .build();
    }
}
