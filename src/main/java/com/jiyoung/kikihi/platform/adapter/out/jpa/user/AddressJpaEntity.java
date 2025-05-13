package com.jiyoung.kikihi.platform.adapter.out.jpa.user;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddressJpaEntity {

    private String postCode;
    private String address;
    private String detailedAddress;
}
