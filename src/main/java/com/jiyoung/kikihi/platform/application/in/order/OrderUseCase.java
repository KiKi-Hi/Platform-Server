
package com.jiyoung.kikihi.platform.application.in.order;


import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.DeliveryInfoRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.OrderRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.order.OrderResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.order.PaymentReadyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface OrderUseCase {

    /**
     * - 배송 정보 저장     *
     *
     * @param deliveryInfoRequest 배송 정보 요청
     */
    void saveDeliveryInfo(DeliveryInfoRequest deliveryInfoRequest,
                          UUID userId);


    /**
     * 주문 요청을 처리합니다.
     * - 배송 정보 저장
     * - 상품 및 가격 정보 조회 (Redis)
     * - 결제 준비 (예: KakaoPay 등)
     *
     * @param orderRequest 주문 요청 정보
     * @return 결제 준비 응답 정보 (결제 URL 등)
     */
    PaymentReadyResponse createOrder(OrderRequest orderRequest, UUID userId);

    /**
     * 사용자가 결제 후 콜백을 통해 주문을 검증합니다.
     * - 결제 상태 확인
     * - 최종 주문 정보 저장
     *
     * @param pgToken 결제 승인 토큰
     * @param orderId 주문 ID
     * @return 주문 응답 정보
     */
//    void confirmOrder(String pgToken, UUID orderId);

    /**
     * 특정 사용자의 주문 목록을 조회합니다.
     *
     * @param userId   사용자 ID
     * @param pageable 페이징 정보
     * @return 주문 목록 페이지
     */
    Slice<OrderResponse> getMyOrders(UUID userId, Pageable pageable);

    /**
     * 주문 상세 조회
     *
     * @param orderId 주문 ID
     * @return 주문 상세 정보
     */
//    OrderResponse getOrderDetail(UUID orderId);
}
