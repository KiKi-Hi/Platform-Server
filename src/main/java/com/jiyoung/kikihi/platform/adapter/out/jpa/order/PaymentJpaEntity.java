package com.jiyoung.kikihi.platform.adapter.out.jpa.order;

import com.jiyoung.kikihi.platform.domain.order.OrderState;
import com.jiyoung.kikihi.platform.domain.order.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column
    private UUID orderId;

    @Column(nullable = false)
    private String tossOrderId;     // 토스내부에서 관리하는 별도의 orderId

    @Column(nullable = false, unique = true)
    private String tossPaymentKey;

    private int totalAmount;

    @Column(nullable = false)
    private String tossOrderName;

    @Column(nullable = false)
    private String tossPaymentMethod;

    @Column(nullable = false)
    private OrderState tossPaymentStatus;

    @Column(nullable = false)
    private LocalDateTime approvedAt;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    public static PaymentJpaEntity fromDomain(Payment payment) {
        return PaymentJpaEntity.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .tossOrderId(payment.getTossOrderId())
                .tossPaymentKey(payment.getTossPaymentKey())
                .totalAmount(payment.getTotalAmount())
                .tossOrderName(payment.getTossOrderName())
                .tossPaymentMethod(payment.getTossPaymentMethod())
                .tossPaymentStatus(OrderState.valueOf(payment.getTossPaymentStatus()))
                .approvedAt(payment.getApprovedAt())
                .requestedAt(payment.getRequestedAt())
                .build();
    }

    public Payment toDomain(PaymentJpaEntity paymentJpaEntity) {
        return Payment.builder()
                .paymentId(paymentJpaEntity.getPaymentId())
                .orderId(paymentJpaEntity.getOrderId())
                .tossOrderId(paymentJpaEntity.getTossOrderId())
                .tossPaymentKey(paymentJpaEntity.getTossPaymentKey())
                .totalAmount(paymentJpaEntity.getTotalAmount())
                .tossOrderName(paymentJpaEntity.getTossOrderName())
                .tossPaymentMethod(paymentJpaEntity.getTossPaymentMethod())
                .tossPaymentStatus(paymentJpaEntity.getTossPaymentStatus().name())
                .approvedAt(paymentJpaEntity.getApprovedAt())
                .requestedAt(paymentJpaEntity.getRequestedAt())
                .build();
    }
}
