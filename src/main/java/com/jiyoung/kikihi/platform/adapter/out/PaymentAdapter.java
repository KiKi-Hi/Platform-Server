package com.jiyoung.kikihi.platform.adapter.out;


import com.jiyoung.kikihi.platform.adapter.out.jpa.order.PaymentJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository.PaymentJpaRepository;
import com.jiyoung.kikihi.platform.application.out.order.PaymentPort;
import com.jiyoung.kikihi.platform.domain.order.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentPort {

    private final PaymentJpaRepository paymentJpaRepository;


    // 결제 저장
    @Override
    public Payment savePayment(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = PaymentJpaEntity.fromDomain(payment);
        PaymentJpaEntity returnedPayment=paymentJpaRepository.save(paymentJpaEntity);
        return returnedPayment.toDomain(returnedPayment);
    }

//
//    @Override
//    public Optional<Payment> loadPayment(Long id) {
//        return paymentJpaRepository.findById(id);
//    }
//
//    @Override
//    public Optional<Payment> loadPaymentByPaymentKey(String paymentKey) {
//        return paymentJpaRepository.findByPaymentKey(paymentKey);
//    }
//
//    @Override
//    public Slice<Payment> loadPaymentsByUserId(Long userId, Pageable pageable) {
//        return paymentJpaRepository.findAllByUserId(userId, pageable);
//    }

//    @Override
//    public void deletePayment(Long paymentId) {
//        paymentJpaRepository.deleteById(paymentId);
//    }

}


