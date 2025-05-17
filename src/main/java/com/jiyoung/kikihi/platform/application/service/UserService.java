package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.security.oauth2.kakao.KaKaoDto;
import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.application.in.auth.AuthUseCase;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.jiyoung.kikihi.platform.domain.user.Role.USER;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements AuthUseCase {

    private final UserPort userPort;

    // 회원가입
    @Override
    public User joinUser(KaKaoDto.KakaoUserInfoResponse userInfo) {

        /// 이메일 중복 체크
        boolean emailUsed = isEmailUsed(userInfo.kakao_account().email());

        if (emailUsed) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User joinUser = User.builder()
                .name(userInfo.kakao_account().profile().nickname())
                .email(userInfo.kakao_account().email())
                .profileImage(userInfo.kakao_account().profile().profileImage()) // 안들어감
                .role(USER)
                .build();
        User savedUser=userPort.saveUser(joinUser);
        return savedUser;
    }

    private boolean isEmailUsed(String email) {
        return userPort.existsByEmail(email);
    }


    public User findByKakaoId(Long kakaoId) {
        return userPort.findByKakaoId(kakaoId);
    }

}
