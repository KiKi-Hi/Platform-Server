package com.kikihi.store.platform.adapter.out;

import com.kikihi.store.platform.adapter.out.mongo.product.ProductDocument;
import com.kikihi.store.platform.adapter.out.mongo.product.ProductDocumentRepository;
import com.kikihi.store.platform.application.out.product.ProductPort;
import com.kikihi.store.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MongoDB 와 ElasticSearch 통해 기능 구현
 */

@Component
@RequiredArgsConstructor
public class ProductMongoAdapter implements ProductPort {

    private final ProductDocumentRepository documentRepository;

    @Override
    public List<Product> getProducts() {

        // DB 가져오기
        List<ProductDocument> response = documentRepository.findAll();

        // 변환하기
        return response.stream()
                .map(ProductDocument::toDomain)
                .toList();
    }

    // 카테고리 기반 상품 목록 조회 (카테고리만)
    @Override
    public Slice<Product> getProducts(String category, Pageable pageable) {

        /// DB 조회
        Slice<ProductDocument> result = documentRepository
                .findByCategory(category, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    // 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    @Override
    public Slice<Product> getProducts(String category, List<String> manufacturer, Pageable pageable) {

        /// DB 조회
        Page<ProductDocument> result = documentRepository
                .findByCategoryAndManufacturer(category, manufacturer, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    // 카테고리 기반 상품 목록 조회 (카테고리, 가격 포함)
    @Override
    public Slice<Product> getProducts(String category, Integer minPrice, Integer maxPrice, Pageable pageable) {
        /// DB 조회
        Slice<ProductDocument> result = documentRepository
                .findByCategoryAndPriceRange(category, minPrice, maxPrice, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    // 카테고리 기반 상품 목록 조회 (카테고리, 제조사, 가격 포함)
    @Override
    public Slice<Product> getProducts(String category, List<String> manufacturer,
                                     Integer minPrice, Integer maxPrice, Pageable pageable) {

        /// DB 조회
        Slice<ProductDocument> result = documentRepository
                .findByCategoryAndManufacturerAndPriceRange(category, manufacturer, minPrice, maxPrice, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    @Override
    public Optional<Product> getProduct(String productId) {

        /// DB 조회
        return documentRepository.findById(productId)
                .map(ProductDocument::toDomain);
    }


    @Override
    public void deleteProduct(String productId) {

    }

    @Override
    public List<Product> getProductsByRecommendation(String category) {
        return List.of();
    }

    @Override
    public List<Product> getRandomProductsExcludeIds(List<String> excludedIds, int limit) {
        return documentRepository.findRandomExcludeIds(excludedIds, limit).stream()
                .map(ProductDocument::toDomain)
                .toList();
    }

    @Override
    public Slice<Product> getProductsByIds(List<String> productIds, Pageable pageable) {
        return documentRepository.findByIdIn(productIds, pageable)
                .map(ProductDocument::toDomain);
    }

    @Override
    public Map<String, Product> getProductsByIds(List<String> productIds) {
        return documentRepository.findByIdIn(productIds).stream()
                .map(ProductDocument::toDomain)
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

}
