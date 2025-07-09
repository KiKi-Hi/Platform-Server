package com.jiyoung.kikihi.platform.application.out.order;

import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * 상품을 장바구니에 넣고 조회하는 유스 케이스입니다.
 * 장바구니에 담기 - productId, 수량, 사용자 ID를 받아 장바구니에 상품을 담는다.
 * 장바구니 조회 - cartId를 받아 해당 장바구니에 담긴 상품 목록을 조회한다. 최종 결제 금액을 계산한다
 * 장바구니에 담은 상품 삭제 - cartId와 productId를 받아 장바구니에서 해당 상품을 삭제한다.
 * 장바구니에 담은 상품 수량 변경 - cartId와 productId, 수량을 받아 장바구니에 담긴 상품의 수량을 변경한다.
 */
public interface CartPort {

    // 저장
    void saveCartItem(String productId, Integer quantity, UUID userId);

    // 조회
    List<Product> getCartItems(UUID userId);

    // 삭제
    void deleteCartItem(String productId, UUID userId);

    // 수량 변경
    void updateCartItemQuantity(String productId, Integer quantity, UUID userId);

}
