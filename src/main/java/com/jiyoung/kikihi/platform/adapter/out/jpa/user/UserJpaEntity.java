package com.jiyoung.kikihi.platform.adapter.out.jpa.user;

import com.jiyoung.kikihi.platform.adapter.out.jpa.BaseTimeEntity;
import com.jiyoung.kikihi.platform.domain.user.Role;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@AllArgsConstructor
@Builder
public class UserJpaEntity extends BaseTimeEntity {

    @Id
    private UUID id;

    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImage;

    @Embedded
    private AddressJpaEntity address;

    @PrePersist
    public void generateUUID() {
        this.id = UUID.randomUUID();
    }
}
