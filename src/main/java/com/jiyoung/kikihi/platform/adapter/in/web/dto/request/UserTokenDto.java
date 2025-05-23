package com.jiyoung.kikihi.platform.adapter.in.web.dto.request;

import com.jiyoung.kikihi.platform.domain.user.Role;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

/**
 * 토큰에서 가져올 수 있는 유저 정보
 * **/

@Builder
public record UserTokenDto(
        UUID id,
        String email,
        Role role
) {

    public static UserTokenDto of(UUID id, String email, Role role) {
        return UserTokenDto.builder()
                .id(id)
                .email(email)
                .role(role)
                .build();
    }

    public static UserTokenDto from(User user) {
        return UserTokenDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
