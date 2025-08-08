package com.kikihi.store.platform.application.in.product;

import com.kikihi.store.platform.adapter.in.web.dto.response.product.ProductDetailResponse;
import com.kikihi.store.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.kikihi.store.platform.domain.custom.CustomKeyboardLayout;
import com.kikihi.store.platform.domain.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.*;

/**
 * 제품의 조회 기능을 담당하는 유즈 케이스입니다.
 * 상품 목록 조회 - 무한 스크롤 방식으로 진행하여, 한번에 20개씩 불러온다.
 * 상품 목록 조회 - [썸네일, 제조사명, 제품명, 할인율, 할인가, 나의 좋아요 여부]를 보여준다.
 * 상품 목록 조회 - 체크박스로 여러 개의 제조사 상품을 조회할 수 있다.
 * 상품 상세 조회 - [ 상품 이미지 (최대 5개 - 캐러셀 형식), 제조사, 제품명, 할인율, 정가, 할인가, 배송 정보(배송비, 배송 종류, 배송 날짜), 어울리는 상품 추천, 상품 유의사항, 상세 정보 이미지] 의 정보를 제공한다.
 */

public interface ProductUseCase {

    /// 상품 목록 조회
    // 상품 목록 조회 (페이징X) - mongoDB
    List<Product> getProducts();

    // 카테고리별 목록 조회 (카테고리 포함) - mongoDB
    Slice<ProductListResponse> getProductsByCategoryId(UUID userId, String categoryId, Pageable pageable);

    // 카테고리별 목록 조회 (카테고리, 제조사 포함)
    Slice<ProductListResponse> getProductsByCategoryIdAndManufacturerId(UUID userId, String categoryId, List<String> manufacturerId, Pageable pageable);

    // 카테고리별 목록 조회 (카테고리, 가격 포함)
    Slice<ProductListResponse> getProductsByCategoryIdAndPrice(UUID userId, String categoryId, Integer minPrice, Integer maxPrice, Pageable pageable);

    // 카테고리별 목록 조회 (카테고리, 제조사, 가격 포함)
    Slice<ProductListResponse> getProductsByCategoryIdAndManufacturerIdAndPrice(UUID userId, String categoryId, List<String> manufacturer, Integer minPrice, Integer maxPrice, Pageable pageable);

    /// 상품 상세 조회
    ProductDetailResponse getProduct(UUID userId, String id);

    /// 상품 추천
    List<Product> getProductsByRecommendation();

    /// 외부 의존성
    // 커스텀 키보드에 맞는 부품들 조회
    Slice<ProductListResponse> getProductsByLayout(UUID userId, String categoryId, CustomKeyboardLayout layout, Pageable pageable);


}
