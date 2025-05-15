package com.jiyoung.kikihi.platform.adapter.out.jpa.user;

import com.jiyoung.kikihi.platform.adapter.out.jpa.BaseTimeEntity;
import com.jiyoung.kikihi.platform.domain.user.Address;
import com.jiyoung.kikihi.platform.domain.user.Role;
import com.jiyoung.kikihi.platform.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@AllArgsConstructor
@Builder
public class UserJpaEntity extends BaseTimeEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImage;

    @Embedded
    private Address address;

    @PrePersist
    public void generateUUID() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public static UserJpaEntity from(User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .profileImage(user.getProfileImage())
                .address(user.getAddress())
                .build();
    }

    public static User toDomain(UserJpaEntity userJpaEntity) {
        return User.builder()
                .id(userJpaEntity.getId())
                .name(userJpaEntity.getName())
                .email(userJpaEntity.getEmail())
                .phoneNumber(userJpaEntity.getPhoneNumber())
                .role(userJpaEntity.getRole())
                .profileImage(userJpaEntity.getProfileImage())
                .address(userJpaEntity.getAddress())
                .build();
    }
}
