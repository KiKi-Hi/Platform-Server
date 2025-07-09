package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.order.CartJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository.CartJpaRepository;
import com.jiyoung.kikihi.platform.adapter.out.jpa.order.repository.OrderJpaRepository;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocument;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocumentRepository;
import com.jiyoung.kikihi.platform.application.out.order.CartPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartAdapter implements CartPort {
    private final OrderJpaRepository orderJpaRepository;
    private final CartJpaRepository cartJpaRepository;
    private final ProductDocumentRepository productDocumentRepository;

    // 장바구니에 저장
    @Override
    public void saveCartItem(String productId, Integer quantity, UUID userId) {
        var cartItem= CartJpaEntity.builder()
                .id(UUID.randomUUID()) // 새 UUID 생성
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .build();

        cartJpaRepository.save(cartItem);
    }

    // 장바구니 조회 -> 상품 목록 조회
    /*
    * userId로 cart조회
    * cart에 있는 상품id 조회
    * mongoDB에서 productId 로 상품 목록 조회
    *
    * */
    @Override
    public List<Product> getCartItems(UUID userId) {

        // ✨ 장바구니 상품 ID 추출 및 외부 조회
        List<String> productIds = cartJpaRepository.findProductIdsByUserId(userId); // Cart 하나만 반환
        log.info(productIds.toString());

        // ✨ productId를 기반으로 mongo DB에서 상품 정보를 조회합니다.
        List<ProductDocument> productDocuments= productDocumentRepository.findByProductIdIn(productIds);
        log.info(productDocuments.toString());

        // ProductDocument를 Product로 변환
        return productDocuments.stream()
                .map(ProductDocument::toDomain)
                .toList();

    }

    @Override
    public void deleteCartItem(String productId, UUID userId) {
        cartJpaRepository.deleteByProductId(productId);

    }

    @Override
    public void updateCartItemQuantity(String productId, Integer quantity, UUID userId) {
        CartJpaEntity cartItem = (CartJpaEntity) cartJpaRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니에 해당 상품이 없습니다."));

        cartItem.setQuantity(quantity);
        cartJpaRepository.save(cartItem);

    }
}
