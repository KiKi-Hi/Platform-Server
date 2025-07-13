package com.jiyoung.kikihi.platform.domain.order;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.OrderProductRequest;
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
public class OrderProducts {
    private Long id;
    private String productId;
    private UUID orderId;
    private Integer quantity;
    private Double price;

    public static OrderProducts of(OrderProductRequest orderProductList) {
        return OrderProducts.builder()
                .productId(orderProductList.productId())
                .quantity(orderProductList.quantity())
                .price(orderProductList.price())
                .build();
    }
}
