package site.kikihi.custom.platform.adapter.out.jpa.user;

import site.kikihi.custom.platform.adapter.out.jpa.BaseTimeEntity;
import site.kikihi.custom.platform.domain.user.Provider;
import site.kikihi.custom.platform.domain.user.Role;
import site.kikihi.custom.platform.domain.user.User;
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

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImage;

    @Embedded
    private AddressJpaEntity address;

    private boolean isSearch;

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
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .profileImage(user.getProfileImage())
                .address(AddressJpaEntity.from(user.getAddress()))
                .isSearch(user.isSearch())
                .build();
    }

    public User toDomain() {
        return User.builder()
                .id(id)
                .provider(provider)
                .socialId(socialId)
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(role)
                .profileImage(profileImage)
                .address(address.toDomain())
                .isSearch(isSearch)
                .build();
    }

}
