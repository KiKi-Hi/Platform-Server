package com.jiyoung.kikihi.platform.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.ConfirmPaymentRequest;
import com.jiyoung.kikihi.platform.application.in.order.PaymentUseCase;
import com.jiyoung.kikihi.platform.application.out.order.PaymentPort;
import com.jiyoung.kikihi.platform.domain.order.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentSerivce implements PaymentUseCase {

    @Value("${payment.toss.secret_key}")
    private String tossSecretKey;

    private final PaymentPort paymentPort;

    String encodedAuth = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
    String authorizations = "Basic " + encodedAuth;
    ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 토스에게 결제 승인 요청
     */
    @Override
    public HttpResponse<String> requestConfirm(ConfirmPaymentRequest request) throws IOException, InterruptedException {
        String tossOrderId = request.orderId();
        int tossAmount = request.amount();
        String tossPaymentKey = request.paymentKey();

        // 요청 객체 생성
        JsonNode requestObj = objectMapper.createObjectNode()
                .put("paymentKey", tossPaymentKey)
                .put("orderId", tossOrderId)
                .put("amount", tossAmount);

        String requestBody = objectMapper.writeValueAsString(requestObj);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", authorizations)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    /**
     * 결제 취소 요청
     */
    public HttpResponse<String> requestPaymentCancel(String paymentKey, String cancelReason) throws IOException, InterruptedException {
        System.out.println(paymentKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                .header("Authorization", authorizations)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("cancelReason:" + cancelReason))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public void savePayment(ConfirmPaymentRequest confirmPaymentRequest, HttpResponse<String> response) throws IOException, InterruptedException {
        // 이미 Toss로부터 받은 response
        JsonNode responseBody = objectMapper.readTree(response.body());

        if (response.statusCode() == 200) {
            Payment payment = createPayment(confirmPaymentRequest, responseBody);
            Payment saved = paymentPort.savePayment(payment); // 반환할지 말지 논의


        } else {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 내부 메서드
    private static Payment createPayment(ConfirmPaymentRequest confirmPaymentRequest, JsonNode responseBody) {
        return Payment.builder()
                .tossPaymentKey(confirmPaymentRequest.paymentKey())
                .tossOrderId(confirmPaymentRequest.orderId())
                .totalAmount(confirmPaymentRequest.amount())
                .tossPaymentMethod(responseBody.get("method").asText())
                .tossPaymentStatus(responseBody.get("status").asText())
                .tossOrderName(responseBody.get("orderName").asText())
                .approvedAt(OffsetDateTime.parse(responseBody.get("approvedAt").asText()).toLocalDateTime())
                .requestedAt(OffsetDateTime.parse(responseBody.get("requestedAt").asText()).toLocalDateTime())
                .orderId(UUID.fromString(responseBody.get("orderId").asText())) // UUID orderId, 형식 주의
                .build();
    }

}
