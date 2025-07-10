package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.CartJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocument;
import com.jiyoung.kikihi.platform.application.in.order.CartUseCase;
import com.jiyoung.kikihi.platform.application.out.order.CartPort;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService implements CartUseCase {

    private final CartPort cartPort;
    private final ProductPort productPort;

    @Override
    public void addProductToCart(String productId, Integer quantity, UUID userId) {
        Optional<CartJpaEntity> optionalCartItem = cartPort.findCartProductByProductIdAndUserId(productId, userId);

        if (optionalCartItem.isPresent()) {
            updateCartItemQuantityAndSave(optionalCartItem.get(), quantity);
        } else {
            //product가 있는지 확인하고 그 productId를 넣어야함
            productPort.getProduct(productId).ifPresent(product -> {
                cartPort.saveCartItem(CartJpaEntity.from(product.getId(), userId, quantity));
            });
        }
    }

    @Override
    public List<Product> getCartProducts(UUID userId) {
        List<String> productIds = cartPort.getCartProductIds(userId);
        if (productIds.isEmpty()) {
            throw new NoSuchElementException(ErrorCode.CART_NOT_FOUND.getMessage());
        }
        // ✨ productId를 기반으로 mongo DB에서 상품 정보를 조회합니다
        return productIds.stream()
                .map(productId -> productPort.getProduct(productId)
                        .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage())))
                .toList();
    }

    @Override
    public void removeProductFromCart(String productId, UUID userId) {
        cartPort.deleteCartItem(productId, userId);

    }

    @Override
    public void updateProductQuantityInCart(String productId, Integer quantity, UUID userId) {
        CartJpaEntity cartItem = cartPort.findCartProductByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니에 해당 상품이 없습니다."));

        updateCartItemQuantityAndSave(cartItem, quantity);
    }

    private void updateCartItemQuantityAndSave(CartJpaEntity cartItem, Integer quantity) {
        cartItem.setQuantity(quantity);
        cartPort.saveCartItem(cartItem);
    }
}
