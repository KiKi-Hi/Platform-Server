package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductDetailResponse;
import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;

    // 상품 목록 조회
    /**
     * 최신 상품 목록 조회
     */
    @Override
    public List<Product> getProducts() {
        return productPort.getProducts();
    }

    /**
     * 카테고리별 목록 조회 (카테고리 포함)
     * @param categoryId    카테고리 ID
     * @param pageable      페이지
     */
    @Override
    public Slice<Product> getProductsByCategoryId(String categoryId, Pageable pageable) {

        /// 카테고리를 바탕으로 조회
        return productPort.getProducts(categoryId, pageable);
    }

    /**
     * 카테고리별 목록 조회 (카테고리, 제조사 포함)
     * @param categoryId        카테고리 ID
     * @param manufacturers     조회할 제조사
     * @param pageable          페이지
     */
    @Override
    public Slice<Product> getProductsByCategoryIdAndManufacturerId(String categoryId, List<String> manufacturers, Pageable pageable) {

        return productPort.getProducts(categoryId, manufacturers, pageable);
    }

    //

    /**
     * 카테고리별 목록 조회 (카테고리, 가격 포함)
     * @param categoryId    카테고리 ID
     * @param minPrice      최소 가격
     * @param maxPrice      최대 가격
     * @param pageable      페이지
     */
    @Override
    public Slice<Product> getProductsByCategoryIdAndPrice(String categoryId, Integer minPrice, Integer maxPrice, Pageable pageable) {

        return productPort.getProducts(categoryId, minPrice, maxPrice, pageable);
    }


    /**
     * 카테고리별 목록 조회 (카테고리, 제조사, 가격 포함)
     * @param categoryId        카테고리 ID
     * @param manufacturers     제조사
     * @param minPrice          최소 가격
     * @param maxPrice          최대 가격
     * @param pageable          페이지
     */
    @Override
    public Slice<Product> getProductsByCategoryIdAndManufacturerIdAndPrice(String categoryId, List<String> manufacturers, Integer minPrice, Integer maxPrice, Pageable pageable) {

        return productPort.getProducts(categoryId, manufacturers, minPrice, maxPrice, pageable);
    }

    // 상품 상세 조회
    /**
     * 상품 상세 조회
     * @param id    조회할 상품 ID
     */
    @Override
    public ProductDetailResponse getProduct(String id) {

        /// 상품 조회
        Product product = loadProduct(id);

        /// DTO 변환
        ProductDetailResponse response = ProductDetailResponse.from(product);

        // 추가해야할 내용
        // TODO! 배송정보, 추천 아이템, 등등 추가로 설정하기

        return response;
    }

    // 상품 추천 목록 조회

    /**
     * 추천 서비스를 구현 합니다!
     */
    @Override
    public List<Product> getProductsByRecommendation() {
        return List.of();
    }


    // 공통 함수
    /**
     * 상품을 가져오는 함수 입니다.
     * @param id    상품 ID
     */
    private Product loadProduct(String id) {
        return productPort.getProduct(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }
}
