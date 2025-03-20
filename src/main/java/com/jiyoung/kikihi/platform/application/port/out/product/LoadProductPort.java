package com.jiyoung.kikihi.platform.application.port.out.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoadProductPort {

    // 상품 정보 가져오기
    Optional<Product> loadProductById(Long productId);

    // 최신순 가져오기
    Page<Product> loadProducts(Pageable pageable, Long categoryId);

    // 좋아요수 조회
    Page<Product> loadProductsByLike(Pageable pageable, Long categoryId);

    // 조건에 따른 조회
    Page<Product> loadProductsByCondition(Pageable pageable, Long categoryId, String productTitle, Double minPrice, Double maxPrice);

    // 상품 재고 조회
    Integer findStockByProductId(Long productId);


}
