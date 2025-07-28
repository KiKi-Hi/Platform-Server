package com.jiyoung.kikihi.platform.application.service;

import com.jiyoung.kikihi.platform.adapter.out.elasticSearch.ProductESDocument;
import com.jiyoung.kikihi.platform.adapter.out.elasticSearch.ProductESRepository;
import com.jiyoung.kikihi.platform.application.in.product.ProductSearchUseCase;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticSearchService implements ProductSearchUseCase {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductESRepository productESRepository;

//    // 상품 저장
//    public Product saveProduct(Product product) {
//        ProductESDocument doc = ProductESDocument.toESDocument(product);
//        ProductESDocument saved = productESRepository.save(doc);
//        return toDomain(saved);
//    }

    // 키워드 검색 (name, description)
    @Override
    public List<Product> searchProducts(String keyword, int page, int size, float minScore) {
        // match 쿼리 구성
        Query nameMatch = MatchQuery.of(m -> m.field("name").query(keyword))._toQuery();
        Query descMatch = MatchQuery.of(m -> m.field("description").query(keyword))._toQuery();

        // bool 쿼리
        Query boolQuery = BoolQuery.of(b -> b
                .should(nameMatch)
                .should(descMatch)
                .minimumShouldMatch("1")
        )._toQuery();

        // NativeQuery
        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(page, size)) //page-> from으로 자동 변환(from=page * size)
                .withMinScore(minScore)  // <<-- 추가됨
                .build();

        return elasticsearchOperations.search(query, ProductESDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .map(ProductESDocument::toDomain)
                .collect(Collectors.toList());
    }


    // 필터링
    @Override
    public List<Product> filterProducts(String keyword, String manufacturer, Double minPrice, Double maxPrice, int page, int size) {
        // 1. should 쿼리(키워드 검색)
        List<Query> shouldQueries = new ArrayList<>();
        shouldQueries.add(MatchQuery.of(m -> m.field("productName").query(keyword))._toQuery());
        shouldQueries.add(MatchQuery.of(m -> m.field("description").query(keyword))._toQuery());

        // 2. must 쿼리(제조사, 가격)
        List<Query> mustQueries = new ArrayList<>();
        if (manufacturer != null && !manufacturer.isEmpty()) {
            mustQueries.add(MatchQuery.of(m -> m.field("manufacturer").query(manufacturer))._toQuery());
        }
//        if (minPrice != null || maxPrice != null) {
//            RangeQuery rangeQuery = RangeQuery.of(r -> r
//                    .field("price")
//                    .gte(minPrice != null ? JsonData.of(minPrice) : null)
//                    .lte(maxPrice != null ? JsonData.of(maxPrice) : null)
//            );
//            mustQueries.add(rangeQuery._toQuery());
//        }

        // 3. BoolQuery 조립
        Query boolQuery = BoolQuery.of(b -> b
                .should(shouldQueries)
                .must(mustQueries)
                .minimumShouldMatch("1")
        )._toQuery();

        // 4. NativeQuery 생성
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(page, size))
                .build();

        // 5. 검색 및 변환
        return elasticsearchOperations.search(nativeQuery, ProductESDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .map(ProductESDocument::toDomain)
                .collect(Collectors.toList());
    }


}
