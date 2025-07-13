package com.jiyoung.kikihi.platform.application.service;


import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.DeliveryInfoRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.order.OrderResponse;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.DeliveryInfoJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.user.UserJpaEntity;
import com.jiyoung.kikihi.platform.application.in.order.OrderUseCase;
import com.jiyoung.kikihi.platform.application.out.order.DeliveryPort;
import com.jiyoung.kikihi.platform.application.out.order.OrderPort;
import com.jiyoung.kikihi.platform.application.out.order.RedisOrderPort;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.order.DeliveryInfo;
import com.jiyoung.kikihi.platform.domain.user.Address;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final UserPort userPort;
    private final DeliveryPort deliveryPort;
    private final RedisOrderPort redisport;
    private final ProductPort productPort;
    private final OrderPort orderPort;


    // 배송 정보 수정
    @Override
    public void saveDeliveryInfo(DeliveryInfoRequest request, UUID userId) {
        Address address = Address.of(
                request.postCode(),
                request.address(),
                request.detailedAddress()
        );

        DeliveryInfo deliveryInfo = DeliveryInfo.of(
                request.recipient(),
                address,
                request.phoneNumber()
        );

        DeliveryInfoJpaEntity deliveryInfoJpaEntity = DeliveryInfoJpaEntity.from(deliveryInfo);
        UUID deliveryInfoId = deliveryPort.saveDeliveryInfo(deliveryInfoJpaEntity);

        User user = UserJpaEntity.from(userPort.loadUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))).toDomain();
        user.setDeliveryInfoId(deliveryInfoId);
        userPort.saveUser(user);
    }

        // 버튼 클릭- 주문 저장- 결제 결과 페이지를 반환해야함
//    @Override
//    @Transactional
//    public PaymentReadyResponse createOrder(OrderRequest orderRequest, UUID userId) {
//
//        // 주문 중복(아이템포턴시) 검증 - redis에서
//        if (redisport.isProcessed(orderRequest.idempotencyKey())) {
//            throw new CustomException(ErrorCode.ORDER_ALREADY_PROCESSED);
//        }
//
//        // 수량 한도, 최대 가격
//        validateOrderRequest(orderRequest);
//
//        try {
//// DeliveryInfo deliveryInfo, List<OrderItem> items, int totalPrice
//            // 주문 정보 Redis 저장 (주문 ID, 상태 등)
//            RedisOrder redisOrder= RedisOrder.of(
//                    orderRequest.deliveryInfoRequest(),
//                    //
//                    orderRequest.stream()
//                            .map(AddToCartRequest::toOrderItem)
//                            .toList(),
//                    orderRequest.totalPrice(),
//                    "주문 생성 중"
//            );
//            redisport.saveOrder(redisOrder);
//
//            // 9. Idempotency Key 저장 (중복 방지)
//            redisport.markProcessed(idempotencyKey);
//
//            // 10. 응답 반환
//            User user = getUser(userId);
//            Double totalPrice = orderRequest.addToCartRequests().stream()
//                    .mapToDouble(AddToCartRequest::price)
//                    .sum();
//            return PaymentReadyResponse.of(redis의.orderId,내가 직접 생성 orderName,user.getEmail(),user.getPhoneNumber(), , idempotencyKey);
//
//        } finally {
//
//        }
//    }

//    private void validateOrderRequest(OrderRequest orderRequest) {
//        if (orderRequest.addToCartRequests() == null || orderRequest.addToCartRequests().isEmpty()) {
//            throw new CustomException(ErrorCode.ACCESS_DENY);
//        }
//        // 추가 데이터 유효성 및 정책 검증(예: 총 주문 금액 한도 등)
//    }


//    @Override
//    public void confirmOrder(String pgToken, UUID orderId) {
//        // 결제 승인 토큰을 사용하여 결제 상태를 확인하고 주문을 완료합니다.
//        // 주문 정보 조회 및 상태 업데이트
//        // 주문 상세 정보 반환
//        throw new UnsupportedOperationException("주문 확인 기능은 아직 구현되지 않았습니다.");
//    }



    @Override
    public Slice<OrderResponse> getMyOrders(UUID userId, Pageable pageable) {
        // 사용자 ID를 기반으로 주문 목록을 조회합니다.
        // 주문 정보는 페이징 처리되어 반환됩니다.
        // 예시로 빈 페이지를 반환합니다.
        return new SliceImpl<>(Collections.emptyList(), pageable, false);
    }


}
