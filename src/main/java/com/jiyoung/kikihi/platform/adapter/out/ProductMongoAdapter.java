package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocument;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocumentRepository;
import com.jiyoung.kikihi.platform.application.out.product.ProductPort;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MongoDB 와 ElasticSearch 통해 기능 구현
 */

@Component
@RequiredArgsConstructor
public class ProductMongoAdapter implements ProductPort {

    private final ProductDocumentRepository repository;

    @Override
    public Product saveProduct(Product product) {
        return null;
    }

    @Override
    public Product getProduct(String productId) {
        return null;
    }

    @Override
    public List<Product> getProducts() {

        // DB 가져오기
        List<ProductDocument> response = repository.findAll();

        // 변환하기
        return response.stream()
                .map(ProductDocument::toDomain)
                .toList();
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {

        // DB 가져오기
        Page<ProductDocument> responses = repository.findAll(pageable);

        // 변환하기
        return responses
                .map(ProductDocument::toDomain);
    }

    @Override
    public void deleteProduct(String productId) {

    }


}
