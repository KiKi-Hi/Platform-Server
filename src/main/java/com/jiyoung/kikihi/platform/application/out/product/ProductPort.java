package com.jiyoung.kikihi.platform.application.out.product;

import com.jiyoung.kikihi.platform.domain.custom.CustomKeyboardLayout;
import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.*;

/**
 * 상품 관련 Port 입니다.
 * DB 내부 CRUD 담당하는 인터페이스입니다.
 */

public interface ProductPort {

    // =================
    //  상품 상세 조회
    // =================
    /// 조회
    Optional<Product> getProduct(String productId);

    // =================
    //  상품 목록 조회
    // =================
    /// 카테고리 기반 상품 목록 조회 (카테고리만)
    Slice<Product> getProducts(String category, Pageable pageable);

    /// 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    Slice<Product> getProducts(String category, List<String> manufacturer, Pageable pageable);

    /// 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    Slice<Product> getProducts(String category, Integer minPrice, Integer maxPrice, Pageable pageable);

    /// 카테고리 기반 상품 목록 조회 (카테고리, 제조사, 가격 포함)
    Slice<Product> getProducts(String category, List<String> manufacturer, Integer minPrice, Integer maxPrice, Pageable pageable);

    // =================
    //  상품 조회
    // =================
    /// 상품 ID들 바탕으로 조회
    Slice<Product> getProductsByIds(List<String> productIds, Pageable pageable);

    /// 상품 ID들 바탕으로 조회
    Map<String, Product> getProductsByIds(List<String> productIds);

    // =================
    //  상품 추천
    // =================

    /// 특정 ID 제외하고 랜덤 추천
    List<Product> getRandomProductsExcludeIds(List<String> excludedIds, int limit);

    /// 상품 랜덤추천
    List<Product> getProductsRandomly(int limit);

    /// 비슷한 상품 추천
    List<Product> findProductsByAttributes(String switchId, String keycapId, CustomKeyboardLayout layout);

    // =================
    //  상품 삭제
    // =================
    /// 삭제
    void deleteProduct(String productId);


    
}
