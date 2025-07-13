package com.jiyoung.kikihi.platform.adapter.out.jpa.user;

import com.jiyoung.kikihi.platform.adapter.out.jpa.BaseTimeEntity;
import com.jiyoung.kikihi.platform.domain.user.Provider;
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

    private Provider provider;

    private String socialId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImage;

    @Column(name = "delivery_info_id", nullable = true)
    private UUID deliveryInfoId;

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
                .provider(user.getProvider())
                .socialId(user.getSocialId())
                .email(user.getEmail())
                .role(user.getRole())
                .profileImage(user.getProfileImage())
                .deliveryInfoId(user.getDeliveryInfoId())
                .build();
    }

    public User toDomain() {
        return User.builder()
                .id(id)
                .provider(provider)
                .socialId(socialId)
                .name(name)
                .email(email)
                .role(role)
                .profileImage(profileImage)
                .deliveryInfoId(deliveryInfoId)
                .build();
    }

}
