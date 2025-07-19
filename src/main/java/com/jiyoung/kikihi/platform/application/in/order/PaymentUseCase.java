package com.jiyoung.kikihi.platform.application.in.order;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.ConfirmPaymentRequest;
import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * 결제 관련 유스케이스
 */
public interface PaymentUseCase {

    /**
     * 결제 정보를 저장합니다.
     *
     * @param confirmPaymentRequest 결제 승인 요청 정보
     * @param response              토스 결제 응답
     * @return 저장된 결제 정보
     */
    void savePayment(ConfirmPaymentRequest confirmPaymentRequest, HttpResponse<String> response) throws IOException, InterruptedException;


    /**
     * 토스 결제 승인 요청
     *
     * @param confirmPaymentRequestDto 결제 승인 파라미터
     * @return 토스 결제 승인 응답
     */
    HttpResponse<String> requestConfirm(ConfirmPaymentRequest confirmPaymentRequestDto) throws IOException, InterruptedException;

    /**
     * 결제 취소 요청
     *
     * @param paymentKey   결제 키
     * @param cancelReason 취소 사유
     * @return 토스 결제 취소 응답
     */
    HttpResponse<String> requestPaymentCancel(String paymentKey, String cancelReason) throws IOException, InterruptedException;

}
