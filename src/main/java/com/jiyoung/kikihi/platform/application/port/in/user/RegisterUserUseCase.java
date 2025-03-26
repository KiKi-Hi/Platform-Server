//package com.jiyoung.kikihi.platform.application.port.in.user;
//
//import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.UserRequest;
//import com.jiyoung.kikihi.platform.domain.user.;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//public interface RegisterUserUseCase {
//
//    /*
//        회원가입 유즈케이스
//        소셜로그인을 통해 회원가입을 진행한다.
//     */
//
//    User saveUser(UserRequest request);       // 회원가입
//
//    User chkSocialLogin(String socialId, String loginType); // 소셜 로그인 시 가입여부 확인
//
//    User loginUser(String email);       // 로그인 회원정보 가져오기
//
//    User chkEmail(String email);         // 회원가입 이메일 체크
//
//    boolean chkNickName(String nickName);   // 닉네임 중복체크(중복한 값이면 false)
//
//    void chgNickName(UserRequest request);    // 닉네임 변경
//
//    User getUserInfo(UserRequest UserRequest);  // 회원 정보 조회
//
//
//}