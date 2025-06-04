package com.jiyoung.kikihi.security.jwt.util;

import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    @Value("${kikihi.jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    private final static String TOKEN_FORMAT = "refreshToken:%s";
    private final RedisTemplate<String, String> redisTemplate;


    public void setRefreshToken(UUID userId, String value) {
        String key = String.format(TOKEN_FORMAT, userId);
        redisTemplate.opsForValue().set(key, value, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public Object getRefreshToken(UUID userId) {
        String key = String.format(TOKEN_FORMAT, userId);
        Object getObjecet = redisTemplate.opsForValue().get(key);

        if (getObjecet == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        return getObjecet;
    }

    /// 유저 Id를 통해 리프레쉬 토큰 삭제하기
    public boolean deleteRefreshTokenByUserId(UUID userId) {
        String key = String.format(TOKEN_FORMAT, userId);

        Object getObjecet = redisTemplate.opsForValue().get(key);

        if (getObjecet == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

}
