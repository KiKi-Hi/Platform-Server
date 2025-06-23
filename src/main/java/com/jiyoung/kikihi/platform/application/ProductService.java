package com.jiyoung.kikihi.platform.application;

import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /// 상품 목록 조회
    // 상품 목록 조회 - mongoDB
    @Override
    public List<Product> getProducts() {
        return productPort.getProducts();
    }

    // 카테고리별 목록 조회 (카테고리 포함) - mongoDB
    @Override
    public Page<Product> getProductsByCategoryId(String categoryId, Pageable pageable) {

        /// 카테고리를 바탕으로 조회
        return productPort.getProducts(categoryId, pageable);
    }

    // 카테고리별 목록 조회 (카테고리, 제조사 포함)
    @Override
    public Page<Product> getProductsByCategoryIdAndManufacturerId(String categoryId, List<String> manufacturers, Pageable pageable) {

        return productPort.getProducts(categoryId, manufacturers, pageable);
    }

    // 카테고리별 목록 조회 (카테고리, 가격 포함)
    @Override
    public Page<Product> getProductsByCategoryIdAndPrice(String categoryId, Integer minPrice, Integer maxPrice, Pageable pageable) {

        return productPort.getProducts(categoryId, minPrice, maxPrice, pageable);
    }


    // 카테고리별 목록 조회 (카테고리, 제조사, 가격 포함)
    @Override
    public Page<Product> getProductsByCategoryIdAndManufacturerIdAndPrice(String categoryId, List<String> manufacturers, Integer minPrice, Integer maxPrice, Pageable pageable) {

        return productPort.getProducts(categoryId, manufacturers, minPrice, maxPrice, pageable);
    }

    /// 상품 상세 조회
    @Override
    public Product getProduct(String id) {
        return productPort.getProduct(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }
}
