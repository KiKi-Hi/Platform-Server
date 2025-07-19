package com.jiyoung.kikihi.platform.domain.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Payment {

    private Long paymentId;
    private UUID orderId;
    private String tossPaymentKey;
    private String tossOrderId;
    private int totalAmount;
    private String tossOrderName;
    private String tossPaymentMethod;
    private String tossPaymentStatus;

    private LocalDateTime approvedAt;
    private LocalDateTime requestedAt;


}
