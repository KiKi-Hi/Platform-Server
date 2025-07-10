package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.CartJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository.CartJpaRepository;
import com.jiyoung.kikihi.platform.application.out.order.CartPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartAdapter implements CartPort {

    private final CartJpaRepository cartJpaRepository;


    // 장바구니에 저장
    @Override
    public void saveCartItem(String productId, Integer quantity, UUID userId) {

        var cartItem = CartJpaEntity.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .build();

        cartJpaRepository.save(cartItem);
    }

    /*
     * userId로 cart조회
     * cart에 있는 상품id 조회
     * mongoDB에서 productId 로 상품 목록 조회
     *
     * */
    @Override
    public List<String> getCartProductIds(UUID userId) {
        return cartJpaRepository.findProductIdsByUserId(userId);

    }

    @Override
    public void deleteCartItem(String productId, UUID userId) {
        cartJpaRepository.deleteByProductId(productId, userId);

    }

    @Override
    public void updateCartItemQuantity(String productId, Integer quantity, UUID userId) {
        Optional<CartJpaEntity> optionalCartItem = cartJpaRepository.findByProductIdAndUserId(productId, userId);
        if (optionalCartItem.isPresent()) {
            CartJpaEntity cartItem = optionalCartItem.get();
            cartItem.setQuantity(quantity);
            cartJpaRepository.save(cartItem);
        }
    }

    @Override
    public Optional<CartJpaEntity> findCartProductByProductIdAndUserId(String productId, UUID userId) {
        return cartJpaRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public void saveCartItem(CartJpaEntity cartItem) {
        cartJpaRepository.save(cartItem);
    }

}

