package com.jiyoung.kikihi.platform.domain.order;

import com.jiyoung.kikihi.platform.domain.user.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/*
*

 *
* */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Deliveries {
    private Long id;
    private UUID orderId;
    // user의 address와 다름
    private String recipient;
    private Address address;
    private String phoneNumber;
    private String message;
    private String state; // 배송 상태 (예: 준비중, 배송중, 배송완료 등)

}
