package com.jiyoung.kikihi.security.jwt.service;

import com.jiyoung.kikihi.security.jwt.util.CookieUtil;
import com.jiyoung.kikihi.security.jwt.util.JwtTokenExtractor;
import com.jiyoung.kikihi.security.jwt.util.JwtTokenProvider;
import com.jiyoung.kikihi.security.jwt.util.RedisUtil;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtTokenService implements JwtTokenUseCase {

    private final JwtTokenProvider provider;
    private final JwtTokenExtractor extractor;

    /// 레디스
    private final RedisUtil redisUtil;

    /// 쿠키
    private final CookieUtil cookieUtil;

    // 액세스 토큰 생성하기
    @Override
    public void createAccessToken(HttpServletResponse response, Authentication authentication) {
        // 토큰을 발급한다.
        String accessToken = provider.generateAccessToken(authentication);

        // 토큰을 쿠키에 저장한다.
        cookieUtil.setAccessCookie(accessToken, response);

    }

    // 리프레쉬 토큰 생성하기
    @Override
    public void createRefreshToken(HttpServletResponse response, Authentication authentication) {

        // 토큰을 발급한다.
        String refreshToken = provider.generateRefreshToken(authentication);

        // 인증 객체에서 정보 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 해당 토큰을 레디스에 저장한다.
        UUID userId = principalDetails.getId();
        redisUtil.saveRefreshToken(userId, refreshToken);

        // 토큰을 쿠키에 저장한다.
        cookieUtil.setRefreshCookie(refreshToken, response);
    }

    // 재발급 하기
    @Override
    public void reissueByRefreshToken(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에서 리프레쉬 토큰 가져오기
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request)
                .orElseThrow(() -> new NoSuchElementException("쿠키에 RefreshToken 존재하지 않습니다."));

        // 쿠키 검증
        if (!extractor.validateToken(refreshToken)) {
            throw new SecurityException("유효하지 않은 토큰입니다.");
        };

        // 인증 객체에서 정보 가져오기
        Authentication authentication = extractor.getAuthentication(refreshToken);

        // 유저 인증 조회
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 유저와 리프레쉬 토큰이 일치하는지 체크한다.
        boolean checked = redisUtil.checkRefreshTokenAndUserId(refreshToken, principalDetails.getId());
        if (!checked) {
            throw new IllegalStateException("토큰과 유저가 일치하지 않습니다.");
        }

        // 쿠키에 다시 전송하기
        createAccessToken(response, authentication);

    }

    @Override
    public void logout(UUID userId, HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에서 리프레쉬 토큰 가져오기
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request)
                .orElseThrow(() -> new NoSuchElementException("쿠키에 RefreshToken 존재하지 않습니다."));

        // 리프레쉬와 유저가 맞는지 체크
        boolean checked = redisUtil.checkRefreshTokenAndUserId(refreshToken, userId);
        if (!checked) {
            throw new IllegalStateException("토큰과 유저 정보가 일치하지 않습니다");
        }

        // 리프레쉬 쿠키를 null 설정
        cookieUtil.deleteRefreshTokenCookie(response);

        // 액세스 쿠키를 null 설정
        cookieUtil.deleteAccessTokenCookie(response);

        // DB에서도 삭제
        redisUtil.deleteByRefreshToken(refreshToken);
    }

}
