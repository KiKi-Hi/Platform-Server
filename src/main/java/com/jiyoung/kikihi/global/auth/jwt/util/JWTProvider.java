package com.jiyoung.kikihi.global.auth.jwt.util;

import com.jiyoung.kikihi.platform.domain.user.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTProvider {

    public static final String ACCESS_TOKEN_SUBJECT = "Authorization";
    public static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String DUMMY_EMAIL = "dummy_email";

    @Value("${kikihi.jwt.key}")
    private String SECRET_KEY;

    @Value("${kikihi.jwt.access.expiration}")
    private Long accessTokenExpiration;

    // Access Token 생성
    public String generateAccessToken(UUID userId, String email, Role role) {
        return generateToken(userId, email, role);
    }

    // Refresh Token 생성
    public String generateRefreshToken(UUID userId, String email, Role role) {
        return generateToken(userId, email, role);
    }

    // 공통 토큰 생성 메서드
    public String generateToken(UUID userId, String email, Role role) {
        return Jwts.builder()
                .claim(ID_CLAIM, userId)
                .claim(EMAIL_CLAIM, email)
                .claim(ROLE_CLAIM, ROLE_PREFIX + role)
                .claim("tokenType", "REFRESH_TOKEN")
                .setSubject(userId.toString()) // 사용자 정보 (고유식별자)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}
