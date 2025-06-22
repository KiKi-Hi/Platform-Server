package com.jiyoung.kikihi.platform.adapter.out.elasticSearch;

import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductESRepository extends ElasticsearchRepository<ProductDocument, String> {

}
