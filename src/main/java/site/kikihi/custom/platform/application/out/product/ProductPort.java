package site.kikihi.custom.platform.application.out.product;

import org.springframework.data.domain.Page;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import site.kikihi.custom.platform.domain.product.Product;
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
    Page<Product> getProducts(String category, Pageable pageable);

    /// 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    Page<Product> getProducts(String category, List<String> manufacturer, Pageable pageable);

    /// 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    Page<Product> getProducts(String category, Integer minPrice, Integer maxPrice, Pageable pageable);

    /// 카테고리 기반 상품 목록 조회 (카테고리, 제조사, 가격 포함)
    Page<Product> getProducts(String category, List<String> manufacturer, Integer minPrice, Integer maxPrice, Pageable pageable);

    /// 카테고리 기반 제조사 조회
    List<String> getManufacturers(String category);


    // =================
    //  상품 조회
    // =================
    /// 상품 ID들 바탕으로 조회
    Slice<Product> getProductsByIds(List<String> productIds, Pageable pageable);

    /// 상품 ID들 바탕으로 조회
    Map<String, Product> getProductsByIds(List<String> productIds);

    Page<Product> getProductsAndCategoryByType(String type, String categoryId, Pageable pageable);

    List<Product> getProductsAndCategoryByType(String type, String categoryId);

    // 개수
    Long countProductsAndCategoryByType(String type, String categoryId);

    Page<Product> getProductsByCategoryAndCustom(String categoryId, Pageable pageable);

    // 개수
    Long countProductsByCategoryAndCustom(String categoryId);

    List<Product> getProductsByCategoryAndCustom(String categoryId);

    /// 상품 개수 조회
    Long countProductsCount(String category);

    List<Product> getProductsByCategory(String category);

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
