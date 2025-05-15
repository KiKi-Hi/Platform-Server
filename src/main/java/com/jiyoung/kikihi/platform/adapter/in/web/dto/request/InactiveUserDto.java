package com.jiyoung.kikihi.platform.adapter.in.web.dto.request;

import com.jiyoung.kikihi.platform.domain.user.Role;
import lombok.Builder;

@Builder
public record InactiveUserDto(
        Long id,
        String email,
        Role role
) {
}
