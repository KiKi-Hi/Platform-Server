package com.jiyoung.kikihi.platform.adapter.out.redis;

import com.jiyoung.kikihi.platform.domain.order.DeliveryInfo;
import com.jiyoung.kikihi.platform.domain.order.OrderProducts;
import com.jiyoung.kikihi.platform.domain.order.OrderState;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@RedisHash(value = "order", timeToLive = 1800) // 30분 TTL(초 단위)
public class RedisOrder implements Serializable {

    @Id
    private String id; // Redis에서 Key로 사용

    private DeliveryInfo deliveryInfo;
    private List<OrderProducts> orderProducts;
    private int totalPrice;
    private String idempotencyKey;
    private OrderState orderState;
    private LocalDateTime orderTime;



    public static RedisOrder of(DeliveryInfo deliveryInfo, List<OrderProducts> orderProducts, int totalPrice) {
        return RedisOrder.builder()
                .deliveryInfo(deliveryInfo)
                .orderProducts(orderProducts)
                .totalPrice(totalPrice)
                .orderState(OrderState.READY)
                .orderTime(LocalDateTime.now())
                .build();
    }
}
