package com.jiyoung.kikihi.platform.application.port.in.search;

import org.jiyoung.kikihi.platform.adapter.in.web.dto.request.product.ProductJPARequest;
import org.jiyoung.kikihi.platform.domain.keyboard.product.Product;
import org.springframework.data.domain.Limit;

import java.util.List;

public interface ElasticSearchUsecase {
       /*
        elasticSearch로 검색하는 유즈케이스
     */
    
    // 검색될 상품 등록하기
    void saveProduct(ProductJPARequest request);
    // 키워드 상품 목록 검색하기
    List<Product> searchByKeyword(String keyword, Limit of);
  
    // 조건으로 상품 목록 검색하기
//    Page<Product> searchByKeywordWithWildcard(String keyword, Pageable pageable);
}
