package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.application.in.order.CartUseCase;
import com.jiyoung.kikihi.platform.application.out.order.CartPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService implements CartUseCase {

   private final CartPort cartPort;

    @Override
    public void addProductToCart(String productId, Integer quantity, UUID userId) {
        cartPort.saveCartItem(productId, quantity, userId);
    }

    @Override
    public List<Product> getCartProducts(UUID userId) {
        List<Product> products = cartPort.getCartItems(userId);
        if (products.isEmpty()) {
            throw new NoSuchElementException(ErrorCode.CART_NOT_FOUND.getMessage());
        }
        return products;
    }

    @Override
    public void removeProductFromCart(String productId, UUID userId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
        cartPort.deleteCartItem(productId, userId);

    }

    @Override
    public void updateProductQuantityInCart(String productId, Integer quantity, UUID userId){
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
        cartPort.updateCartItemQuantity(productId, quantity, userId);

    }
}
