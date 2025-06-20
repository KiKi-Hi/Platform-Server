package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDocumentRepository extends MongoRepository<ProductDocument, String> {

    // 페이징 처리로 가져오기

}
