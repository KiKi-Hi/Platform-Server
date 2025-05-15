package com.jiyoung.kikihi.platform.adapter.in.web.dto.response;

import com.jiyoung.kikihi.platform.domain.user.Address;
import com.jiyoung.kikihi.platform.domain.user.Role;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

/**
 * 유저 정보 반환
 * **/

@Builder
public record UserResponse(
         UUID id,
         String name,
         String email,
         String password,
         String phoneNumber,
         Role role,
         String profileImage,
         Address address
) {
    public static UserResponse of(
            UUID id,
            String name,
            String email,
            String password,
            String phoneNumber,
            Role role,
            String profileImage,
            Address address
    ) {
        return UserResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(role)
                .profileImage(profileImage)
                .address(address)
                .build();
    }

    public static UserResponse from(Optional<User> user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .profileImage(user.getProfileImage())
                .address(user.getAddress())
                .build();
    }
}
