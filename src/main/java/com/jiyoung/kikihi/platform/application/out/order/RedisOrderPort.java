package com.jiyoung.kikihi.platform.application.out.order;

import com.jiyoung.kikihi.platform.adapter.out.redis.RedisOrder;


/**
 * 주문을 생성하고 주문 정보를 조회하는 유스 케이스입니다.
 */
public interface RedisOrderPort {

    boolean isProcessed(String idempotencyKey);

    String saveOrder(RedisOrder redisOrder);

    void markProcessed(String idempotencyKey);
}
