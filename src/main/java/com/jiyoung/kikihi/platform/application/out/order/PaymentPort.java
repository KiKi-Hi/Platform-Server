package com.jiyoung.kikihi.platform.application.out.order;


import com.jiyoung.kikihi.platform.domain.order.Payment;


public interface PaymentPort {

    // 결제 정보 저장
    Payment savePayment(Payment payment);

    // 결제 상세 조회
//    Optional<Payment> loadPayment(Long id);

    // paymentKey로 결제 상세 조회
//    Optional<Payment> loadPaymentByPaymentKey(String paymentKey);

    // 유저 아이디 바탕 결제 조회
//    Slice<Payment> loadPaymentsByUserId(Long userId, Pageable pageable);

    // 결제 삭제
//    void deletePayment(Long paymentId);

}
