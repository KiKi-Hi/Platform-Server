package com.jiyoung.kikihi.platform.domain.order;

import com.jiyoung.kikihi.platform.domain.user.Address;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DeliveryInfo {

    private Long id;
    private String recipient;

    @Embedded
    private Address address;

    private String phoneNumber;

    public static DeliveryInfo of(String recipient, Address address, String phoneNumber) {
        return DeliveryInfo.builder()
                .recipient(recipient)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
