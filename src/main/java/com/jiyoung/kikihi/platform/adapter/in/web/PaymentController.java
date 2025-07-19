package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.ConfirmPaymentRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.payment.SaveAmountRequest;
import com.jiyoung.kikihi.platform.application.in.order.PaymentUseCase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.net.http.HttpResponse;


/*
    * 결제 관련 API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentUseCase paymentService;

    // 결제 승인
    @PostMapping("/confirm")
    public ApiResponse<?> confirmPayment(@RequestBody ConfirmPaymentRequest confirmPaymentRequest) throws Exception {

        log.info("[🧾 결제 승인 요청 시작]");
        log.info("→ paymentKey: {}", confirmPaymentRequest.paymentKey());
        log.info("→ tossOrderId: {}", confirmPaymentRequest.orderId());
        log.info("→ amount: {}", confirmPaymentRequest.amount());

        // 토스에게 결제 승인 요청
        HttpResponse<String> response = paymentService.requestConfirm(confirmPaymentRequest);

        // 응답 로그
        log.info("[📨 토스 응답 수신]");
        log.info("→ 응답 코드: {}", response.statusCode());
        log.debug("→ 응답 본문: {}", response.body());

        // 응답 코드 확인 → 200이면 정상 처리
        if (response.statusCode() == 200) {
            // 결제 정보 DB에 저장
            log.info("[✅ 결제 승인 성공] DB 저장 시작...");
            paymentService.savePayment(confirmPaymentRequest,response);
            log.info("[💾 DB 저장 완료]");
            return ApiResponse.ok(response.body());
        } else {
            // 실패
            log.warn("[❌ 결제 승인 실패]");
            log.warn("→ 응답 본문: {}", response.body());
            return ApiResponse.fail(new CustomException(ErrorCode.PAYMENT_CONFIRM_FAILED));
        }
    }

    //  임시저장하기
    @PostMapping("/saveAmount")
    public ApiResponse<?> tempsave(HttpSession session, @RequestBody SaveAmountRequest saveAmountRequest) {
        session.setAttribute(saveAmountRequest.orderId(), saveAmountRequest.amount());
        return ApiResponse.ok("성공적으로 임시저장되었습니다.");
    }


    // 임시저장한 내용과 동일한지 파악
    @PostMapping("/verifyAmount")
    public ApiResponse<?> verifyAmount(HttpSession session, @RequestBody SaveAmountRequest saveAmountRequest) {

        Integer amount = (Integer) session.getAttribute(saveAmountRequest.orderId());

        if(amount == null || !amount.equals(saveAmountRequest.amount()))
            return ApiResponse.fail(new CustomException(ErrorCode.AMOUNT_MISMATCH));
        session.removeAttribute(saveAmountRequest.orderId());

        return ApiResponse.ok("결제 금액이 유효합니다.");
    }


}
