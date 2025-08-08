package com.jiyoung.kikihi.platform.application.out.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.*;

/**
 * 상품 관련 Port 입니다.
 * DB 내부 CRUD 담당하는 인터페이스입니다.
 */

public interface ProductPort {

    /// 조회
    Optional<Product> getProduct(String productId);

    // 상품 전체 조회
    List<Product> getProducts();

    // 카테고리 기반 상품 목록 조회 (카테고리만)
    Slice<Product> getProducts(String category, Pageable pageable);

    // 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    Slice<Product> getProducts(String category, List<String> manufacturer, Pageable pageable);

    // 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    Slice<Product> getProducts(String category, Integer minPrice, Integer maxPrice, Pageable pageable);

    // 카테고리 기반 상품 목록 조회 (카테고리, 제조사, 가격 포함)
    Slice<Product> getProducts(String category, List<String> manufacturer, Integer minPrice, Integer maxPrice, Pageable pageable);

    /// 삭제
    void deleteProduct(String productId);

    /// 추천 기능 구현
    List<Product> getProductsByRecommendation(String category);

    List<Product> getRandomProductsExcludeIds(List<String> excludedIds, int limit);
    /**
     * 외부 의존성에서 사용하는 함수
     */
    Slice<Product> getProductsByIds(List<String> productIds, Pageable pageable);

    Map<String, Product> getProductsByIds(List<String> productIds);
}
