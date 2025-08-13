package site.kikihi.custom.platform.adapter.in.web.dto.response.user;

import site.kikihi.custom.platform.domain.user.Address;
import site.kikihi.custom.platform.domain.user.Role;
import site.kikihi.custom.platform.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

/**
 * 유저 정보 반환 응답 DTO
 **/
@Builder
@Schema(
        name = "[응답][유저] 유저 정보 조회 Response",
        description = "유저의 상세 정보를 반환하는 응답 DTO입니다."
)
public record UserResponse(
        @Schema(description = "사용자 UUID", example = "95ea60b2-f63b-434e-afc5-d5e5d6a505e7")
        UUID id,

        @Schema(description = "사용자 이름", example = "홍길동")
        String name,

        @Schema(description = "사용자 이메일", example = "honggildong@example.com")
        String email,

        @Schema(description = "휴대폰 번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "권한(ROLE_USER, ROLE_ADMIN 등)", example = "ROLE_USER", implementation = Role.class)
        Role role,

        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile/95ea60b2.jpg")
        String profileImage,

        @Schema(description = "사용자 주소 정보", implementation = Address.class)
        Address address
) {
    public static UserResponse of(
            UUID id,
            String name,
            String email,
            String phoneNumber,
            Role role,
            String profileImage,
            Address address
    ) {
        return UserResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(role)
                .profileImage(profileImage)
                .address(address)
                .build();
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .profileImage(user.getProfileImage())
                .address(user.getAddress())
                .build();
    }
}
