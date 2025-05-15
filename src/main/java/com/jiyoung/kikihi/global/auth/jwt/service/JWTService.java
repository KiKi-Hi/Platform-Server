package com.jiyoung.kikihi.global.auth.jwt.service;

import com.jiyoung.kikihi.global.auth.jwt.dto.JWTTokenDto;
import com.jiyoung.kikihi.global.auth.jwt.util.JWTExtractor;
import com.jiyoung.kikihi.global.auth.jwt.util.JWTProvider;
import com.jiyoung.kikihi.global.auth.jwt.util.RedisUtil;
import com.jiyoung.kikihi.global.common.response.CustomException;
import com.jiyoung.kikihi.global.common.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.UserTokenDto;
import com.jiyoung.kikihi.platform.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final JWTProvider jwtProvider;
    private final JWTExtractor jwtExtractor;
    private final RedisUtil redisUtil;

    // JWT 토큰 생성
    public JWTTokenDto generateJwtToken(UserTokenDto dto) {
        String accessToken = jwtProvider.generateAccessToken(dto.id(), dto.email(), dto.role());
        String refreshToken = jwtProvider.generateRefreshToken(dto.id(), dto.email(), dto.role());
        redisUtil.setRefreshToken(dto.id(), refreshToken);
        return JWTTokenDto.of(accessToken, refreshToken);
    }

    // Token 재발급
    public JWTTokenDto reissueJwtToken(String refreshToken) {
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
        return generateJwtToken(UserTokenDto.of(userId, email, Role.valueOf(role)));
    }


}
