package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.elasticSearch.ProductESRepository;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocument;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocumentRepository;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    public Page<Product> getProducts(String category, Pageable pageable) {

        /// DB 조회
        Page<ProductDocument> result = documentRepository
                .findByCategory(category, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    // 카테고리 기반 상품 목록 조회 (카테고리, 제조사 포함)
    @Override
    public Page<Product> getProducts(String category, List<String> manufacturer, Pageable pageable) {

        /// DB 조회
        Page<ProductDocument> result = documentRepository
                .findByCategoryAndManufacturer(category, manufacturer, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    // 카테고리 기반 상품 목록 조회 (카테고리, 가격 포함)
    @Override
    public Page<Product> getProducts(String category, Integer minPrice, Integer maxPrice, Pageable pageable) {
        /// DB 조회
        Page<ProductDocument> result = documentRepository
                .findByCategoryAndPriceRange(category, minPrice, maxPrice, pageable);

        return result.
                map(ProductDocument::toDomain);
    }

    // 카테고리 기반 상품 목록 조회 (카테고리, 제조사, 가격 포함)
    @Override
    public Page<Product> getProducts(String category, List<String> manufacturer,
                                     Integer minPrice, Integer maxPrice, Pageable pageable) {

        /// DB 조회
        Page<ProductDocument> result = documentRepository
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


}
