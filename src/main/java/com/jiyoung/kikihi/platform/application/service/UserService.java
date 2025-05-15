package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.global.auth.oauth2.kakao.KaKaoDto;
import com.jiyoung.kikihi.platform.application.in.auth.AuthUseCase;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements AuthUseCase {

    private final UserPort userPort;

    // 회원가입
    @Override
    public User joinUser(KaKaoDto.KakaoUserInfoResponse userInfo) {

        /// 이메일 중복 체크
//        boolean emailUsed = isEmailUsed(joinRequest.email());
//
//        if (emailUsed) {
//            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
//        }

        User joinUser = User.builder()
                .name(userInfo.kakao_account().profile().nickname())
                .email(userInfo.kakao_account().email())
                .profileImage(userInfo.kakao_account().profile().profileImage())
                .build();
        userPort.saveUser(joinUser);
        return joinUser;
    }


    public Optional<User> findByKakaoId(Long kakaoId) {
        return Optional.empty();
    }

}
