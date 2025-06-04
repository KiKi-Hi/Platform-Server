package com.jiyoung.kikihi.security.jwt.util;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.user.User;
import com.jiyoung.kikihi.security.jwt.filter.JwtAuthenticationException;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.jiyoung.kikihi.security.jwt.util.TokenNameUtil.ACCESS_TOKEN_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTExtractor {

    private static final String BEARER = "Bearer ";
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    @Value("${kikihi.jwt.key}")
    private String SECRET_KEY;

    private final UserPort userPort;

    public Optional<String> extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        log.info("쿠키 목록: {}", Arrays.toString(cookies));

        if (cookies == null) {
            log.info("토큰 비었음");
            return Optional.empty();
        }

        Optional<String> token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(ACCESS_TOKEN_COOKIE_NAME))
                .map(Cookie::getValue)
                .findFirst();

        log.info("토큰 추출 완료: {}", token.orElse("없음"));

        return token;
    }



    // 사용자 정보 추출
    public String  getId(String token) {
        return getIdFromToken(token, ID_CLAIM);
    }

    public String getEmail(String token) {
        return getClaimFromToken(token, EMAIL_CLAIM);
    }

    public String getRole(String token) {
        return getClaimFromToken(token, ROLE_CLAIM);
    }

    public Boolean isExpired(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().before(new Date());
    }

    private String getClaimFromToken(String token, String claimName) {
        Claims claims = parseClaims(token);
        return claims.get(claimName, String.class);
    }

    private String getIdFromToken(String token, String claimName) {
        Claims claims = parseClaims(token);
        return claims.get(claimName, String.class);
    }

    private Claims parseClaims(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build();
        return parser.parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            log.info("토큰 유효성 검사 성공: {}", claims);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("토큰 만료됨", e);
            throw new JwtAuthenticationException("토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("토큰 검증 실패: {}", e.getMessage(), e);
            throw new JwtAuthenticationException("토큰이 생성되지 않았습니다."); // 여기가 문제라면 메시지 바꿔도 좋음
        }
    }

    public Authentication getAuthentication(String token) {

        /// JWT 파싱
        Claims claims = parseClaims(token);

        /// 권한 정보 가져오기
        List<String> authoritiesList = (List<String>) claims.get(ROLE_CLAIM);

        // 권한을 SimpleGrantedAuthority로 변환
        Collection<? extends GrantedAuthority> authorities =
                authoritiesList.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // userId를 Long으로 안전하게 변환
        UUID userId = claims.get(ID_CLAIM, UUID.class);

        // 해당 userId로 Member를 조회
        User user = userPort.loadUserById(userId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.USER_NOT_FOUND.getMessage()));

        PrincipalDetails details = PrincipalDetails.of(user);

        /// SecurityContext에 저장하기 위한 UsernamePasswordAuthenticationToken 반환
        return new UsernamePasswordAuthenticationToken(details, token, authorities);
    }

}
