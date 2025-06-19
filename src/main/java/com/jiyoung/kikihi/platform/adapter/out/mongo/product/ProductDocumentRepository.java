package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDocumentRepository extends MongoRepository<ProductDocument, String> {
}
