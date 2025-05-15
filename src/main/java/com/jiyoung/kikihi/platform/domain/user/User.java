package com.jiyoung.kikihi.platform.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class User {

    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private Role role;
    private String profileImage;
    private Address address;

    // 사용자 ID를 생성하는 메소드 (JPA에서의 @PrePersist처럼)
    public void generateUUID() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
