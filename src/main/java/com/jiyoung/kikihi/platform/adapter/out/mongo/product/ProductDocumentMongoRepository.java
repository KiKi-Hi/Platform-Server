package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductDocumentMongoRepository extends MongoRepository<ProductDocument, String> {
    List<ProductDocument> findByProductNameContaining(String productName); // productName으로 검색
    List<ProductDocument> findByCategoryId(Long categoryId); // categoryId로 검색
}
