package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.DeliveryInfoRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.OrderRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.order.PaymentReadyResponse;
import com.jiyoung.kikihi.platform.application.in.order.OrderUseCase;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;

    // 배송 정보 저장 API (update도 가능해야함)
    @PostMapping("/shipping-info")
    public ApiResponse<String> saveShippingInfo(@RequestBody DeliveryInfoRequest request,@AuthenticationPrincipal PrincipalDetails user) {

        orderUseCase.saveDeliveryInfo(request,user.getId());
        return ApiResponse.ok("배송 정보가 저장되었습니다.");
    }

    // 결제 전 redis 임시 주문 저장 API
    @PostMapping("/temporary-order")
    public ApiResponse<PaymentReadyResponse> saveTemporaryOrder(@RequestBody @Valid OrderRequest request, @AuthenticationPrincipal PrincipalDetails user) {
        PaymentReadyResponse response=orderUseCase.createOrder(request,user.getId());
        return ApiResponse.ok(response);
    }

    // 결제 완료 후 주문 정보 저장 API


}
