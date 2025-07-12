package com.jiyoung.kikihi.platform.domain.user;

import java.util.UUID;

public class UserFixtures {

    public static User fakeUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .socialId("dev-kakao-id")
                .email(UUID.randomUUID() + "@example.com")
                .profileImage("http://image-url")
                .phoneNumber("010-1111-1111")
                .name("kakao개발자")
                .provider(Provider.KAKAO) // 테스트용 값 (Enum)
                .role(Role.ADMIN) // 관리자 권한 부여
                .address(Address.of())
                .build();
    }

    public static User createUser(UUID id) {
        return User.builder()
                .id(id)
                .socialId("dev-kakao-id")
                .email(UUID.randomUUID() + "@example.com")
                .profileImage("http://image-url")
                .phoneNumber("010-1111-1111")
                .name("kakao개발자")
                .provider(Provider.KAKAO) // 테스트용 값 (Enum)
                .role(Role.ADMIN) // 관리자 권한 부여
                .address(Address.of())
                .build();
    }

    public static User createUser(UUID id, String name,String socialId) {

        return User.builder()
                .id(id)
                .socialId(socialId)
                .email(UUID.randomUUID() + "@example.com")
                .profileImage("http://image-url")
                .phoneNumber("010-1111-1111")
                .name(name)
                .provider(Provider.KAKAO) // 테스트용 값 (Enum)
                .role(Role.ADMIN) // 관리자 권한 부여
                .address(Address.of())
                .build();
    }


}
