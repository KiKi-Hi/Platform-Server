package com.jiyoung.kikihi.platform.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Address {

    private String postCode;
    private String address;
    private String detailedAddress;

    public static Address of() {
        return Address.builder()
                .postCode("postCode")
                .address("address")
                .detailedAddress("detailedAddress")
                .build();
    }

    public static Address of(String postCode, String address, String detailedAddress) {
        return Address.builder()
                .postCode(postCode)
                .address(address)
                .detailedAddress(detailedAddress)
                .build();
    }
}
