package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.redis.RedisOrder;
import com.jiyoung.kikihi.platform.application.out.order.RedisOrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisOrderAdapter implements RedisOrderPort {

    private final RedisTemplate<String,Object> redisTemplate;
    @Override
    public boolean isProcessed(String idempotencyKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("order:processed:" + idempotencyKey));
    }

    @Override
    public String saveOrder(RedisOrder redisOrder) {
        // 1. id가 없으면 UUID로 생성
        String id = redisOrder.getId();
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
            redisOrder.setId(id);
        }

        // 2. Redis key 생성
        String key = "order:" + id;

        // 3. Redis에 저장 (TTL 30분)
        redisTemplate.opsForValue().set(key, redisOrder, 30, TimeUnit.MINUTES);

        // 4. id 반환
        return id;
    }

    @Override
    public void markProcessed(String idempotencyKey) {
        redisTemplate.opsForValue().set("order:processed:" + idempotencyKey, "Y", 30, TimeUnit.MINUTES);
    }


}

