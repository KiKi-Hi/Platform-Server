package com.jiyoung.kikihi.platform.application.in.order;

import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * 상품을 장바구니에 넣고 조회하는 유스 케이스입니다.
 * 장바구니에 담기 - productId, 수량, 사용자 ID를 받아 장바구니에 상품을 담는다.
 * 장바구니 조회 - cartId를 받아 해당 장바구니에 담긴 상품 목록을 조회한다. 최종 결제 금액을 계산한다
 * 장바구니에 담은 상품 삭제 - cartId와 productId를 받아 장바구니에서 해당 상품을 삭제한다.
 * 장바구니에 담은 상품 수량 변경 - cartId와 productId, 수량을 받아 장바구니에 담긴 상품의 수량을 변경한다.
 */

public interface CartUseCase {

    /**
     * 장바구니에 상품을 담습니다.
     *
     * @param productId 상품 ID
     * @param quantity  수량
     * @param userId    사용자 ID
     */
    void addProductToCart(String productId, Integer quantity, UUID userId);

    /**
     * 장바구니에 담긴 상품 목록을 조회합니다.
     *
     * @param userId   사용자 ID
     * @return 장바구니에 담긴 상품 목록
     */
    List<Product> getCartProducts(UUID userId);

    /**
     * 장바구니에서 상품을 삭제합니다.
     *
     * @param productId 상품 ID
     * @param userId    사용자 ID
     */
    void removeProductFromCart(String productId, UUID userId);

    /**
     * 장바구니에 담긴 상품의 수량을 변경합니다.
     *
     * @param productId 상품 ID
     * @param quantity  변경할 수량
     * @param userId    사용자 ID
     */
    void updateProductQuantityInCart(String productId, Integer quantity, UUID userId);



}
