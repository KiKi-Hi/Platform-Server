package com.jiyoung.kikihi.platform.adapter.out.jpa.user;

import com.jiyoung.kikihi.platform.domain.user.Address;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AddressJpaEntity {

    private String postCode;
    private String address;
    private String detailedAddress;

    public static AddressJpaEntity from(Address address) {
        return AddressJpaEntity.builder()
                .postCode(address.getPostCode())
                .address(address.getAddress())
                .detailedAddress(address.getDetailedAddress())
                .build();
    }

    public Address toDomain(){
        return Address.builder()
                .postCode(postCode)
                .address(address)
                .detailedAddress(detailedAddress)
                .build();
    }

}
