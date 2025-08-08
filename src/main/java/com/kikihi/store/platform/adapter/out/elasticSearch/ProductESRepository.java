package com.kikihi.store.platform.adapter.out.elasticSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductESRepository extends ElasticsearchRepository<ProductESDocument, String> {

}
