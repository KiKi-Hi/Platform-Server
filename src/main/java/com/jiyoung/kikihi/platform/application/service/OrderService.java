package com.jiyoung.kikihi.platform.application.service;


import com.jiyoung.kikihi.global.response.CustomException;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.DeliveryInfoRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.OrderProductRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.OrderRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.order.OrderResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.order.PaymentReadyResponse;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.DeliveryInfoJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.user.UserJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.redis.RedisOrder;
import com.jiyoung.kikihi.platform.application.in.order.OrderUseCase;
import com.jiyoung.kikihi.platform.application.out.order.DeliveryPort;
import com.jiyoung.kikihi.platform.application.out.order.OrderPort;
import com.jiyoung.kikihi.platform.application.out.order.RedisOrderPort;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.application.out.user.UserPort;
import com.jiyoung.kikihi.platform.domain.order.DeliveryInfo;
import com.jiyoung.kikihi.platform.domain.order.OrderProducts;
import com.jiyoung.kikihi.platform.domain.order.OrderState;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.platform.domain.user.Address;
import com.jiyoung.kikihi.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
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

    // redis에 주문 저장
    @Override
    @Transactional
    public PaymentReadyResponse createOrder(OrderRequest orderRequest, UUID userId) {
        validateIdempotency(orderRequest.idempotencyKey());
        DeliveryInfo deliveryInfo = toDeliveryInfo(orderRequest);

        List<OrderProducts> orderProducts = toOrderProducts(orderRequest.orderProductList());

        validateOrderProducts(orderProducts);

        String redisOrderId = saveOrderToRedis(deliveryInfo, orderProducts, orderRequest);

        redisport.markProcessed(orderRequest.idempotencyKey());

        return buildPaymentReadyResponse(redisOrderId, orderRequest.totalPrice());
    }

    // private 메서드

    private void validateIdempotency(String idempotencyKey) {
        if (redisport.isProcessed(idempotencyKey)) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_PROCESSED);
        }
    }

    private DeliveryInfo toDeliveryInfo(OrderRequest orderRequest) {
        Address address = Address.of(
                orderRequest.deliveryInfoRequest().postCode(),
                orderRequest.deliveryInfoRequest().address(),
                orderRequest.deliveryInfoRequest().detailedAddress()
        );
        return DeliveryInfo.of(
                orderRequest.deliveryInfoRequest().recipient(),
                address,
                orderRequest.deliveryInfoRequest().phoneNumber()
        );
    }

    private List<OrderProducts> toOrderProducts(List<OrderProductRequest> orderProductRequests) {
        return orderProductRequests.stream()
                .map(OrderProducts::of)
                .toList();
    }

    private void validateOrderProducts(List<OrderProducts> orderProducts) {
        for (OrderProducts orderProduct : orderProducts) {
            Optional<Product> product = productPort.getProduct(orderProduct.getProductId());

            if (!product.isPresent()) {
                throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
            }
            if (orderProduct.getQuantity() <= 0 || orderProduct.getQuantity() > 100) {
                throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
            }
        }
    }

    private String saveOrderToRedis(DeliveryInfo deliveryInfo, List<OrderProducts> orderProducts, OrderRequest orderRequest) {
        RedisOrder redisOrder = RedisOrder.builder()
                .deliveryInfo(deliveryInfo)
                .orderProducts(orderProducts)
                .totalPrice(orderRequest.totalPrice())
                .idempotencyKey(orderRequest.idempotencyKey())
                .orderState(OrderState.READY)
                .orderTime(LocalDateTime.now())
                .build();
        return redisport.saveOrder(redisOrder);
    }

    private PaymentReadyResponse buildPaymentReadyResponse(String redisOrderId, int amount) {
        String successUrl = "https://yourdomain.com/pay/success";
        String failUrl = "https://yourdomain.com/pay/fail";
        return PaymentReadyResponse.of(UUID.fromString(redisOrderId), amount, successUrl, failUrl);
    }


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
        // 사용자 ID를 기반으로 주문 목록을 조회
        // 주문 정보는 페이징 처리되어 반환
        return new SliceImpl<>(Collections.emptyList(), pageable, false);
    }


}
