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

}
