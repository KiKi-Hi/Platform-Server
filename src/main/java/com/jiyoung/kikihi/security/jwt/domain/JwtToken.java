package com.jiyoung.kikihi.security.jwt.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Builder
@RedisHash
public class JwtToken {

    @Id
    private UUID userId;

    @Indexed
    private String refreshToken;

    @TimeToLive(unit = TimeUnit.DAYS)
    private Long expireTime;

    /// 정적 팩토리 메소드
    public static JwtToken of(UUID userId, String refreshToken, Long expireTime) {
        return JwtToken.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .expireTime(expireTime)
                .build();
    }

}
