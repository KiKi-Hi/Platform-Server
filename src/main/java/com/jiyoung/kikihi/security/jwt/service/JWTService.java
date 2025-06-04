package com.jiyoung.kikihi.security.jwt.service;

import com.jiyoung.kikihi.security.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.security.jwt.util.CookieUtil;
import com.jiyoung.kikihi.security.jwt.util.JWTExtractor;
import com.jiyoung.kikihi.security.jwt.util.JWTProvider;
import com.jiyoung.kikihi.security.jwt.util.RedisUtil;
import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.UserTokenDto;
import com.jiyoung.kikihi.platform.domain.user.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTService {

    private final JWTProvider jwtProvider;
    private final JWTExtractor jwtExtractor;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;

    // JWT 토큰 생성
    public JWTTokenDto generateJwtToken(UserTokenDto dto, HttpServletResponse response) {

        log.info("🔐 JWT 토큰 생성 시작 - User ID: {}, Email: {}, Role: {}", dto.id(), dto.email(), dto.role());

        /// 토큰 생성하기
        String accessToken = jwtProvider.generateAccessToken(dto.id(), dto.email(), dto.role());
        String refreshToken = jwtProvider.generateRefreshToken(dto.id(), dto.email(), dto.role());

        log.info("✅ AccessToken 생성 완료: {}", accessToken);
        log.info("✅ RefreshToken 생성 완료: {}", refreshToken);

        /// 레디스 저장하기
        redisUtil.setRefreshToken(dto.id(), refreshToken);
        log.info("🧠 Redis에 RefreshToken 저장 완료 - key: {}", dto.id());

        /// 쿠키로 전달하기
        cookieUtil.setAccessCookie(accessToken, response);
        cookieUtil.setRefreshCookie(refreshToken, response);

        return JWTTokenDto.of(accessToken, refreshToken);
    }

    // Token 재발급
    public JWTTokenDto reissueJwtToken(String refreshToken, HttpServletResponse response) {
        // refreshToken 만료
        if (jwtExtractor.isExpired(refreshToken)) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        UUID userId = jwtExtractor.getId(refreshToken);
        String redisToken = (String) redisUtil.getRefreshToken(userId);

        if (!redisToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        String email = jwtExtractor.getEmail(refreshToken);
        String role = jwtExtractor.getRole(refreshToken);
        return generateJwtToken(UserTokenDto.of(userId, email, Role.valueOf(role)), response);
    }



}
