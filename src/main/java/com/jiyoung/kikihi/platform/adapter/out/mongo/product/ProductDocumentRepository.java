package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductDocumentRepository extends MongoRepository<ProductDocument, String> {

    Page<ProductDocument> findByCategory(String category, Pageable pageable);

    // 카테고리 기반 목록 조회 (카테고리, 제조사 포함)
    @Query("{ 'category': ?0, 'spec_table.제조회사': { $in: ?1 } }")
    Page<ProductDocument> findByCategoryAndManufacturer(String category, List<String> manufacturer, Pageable pageable);

    // 카테고리 기반 목록 조회 (카테고리, 가격 포함)
    @Query("""
    {
      'category': ?0,
      'price': { $gte: ?1, $lte: ?2 }
    }
    """)
    Page<ProductDocument> findByCategoryAndPriceRange(
            String category,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );

    // 카테고리 기반 목록 조회 (카테고리, 제조사, 가격 포함)
    @Query("""
    {
      'category': ?0,
      'spec_table.제조회사': { $in: ?1 },
      'price': { $gte: ?2, $lte: ?3 }
    }
    """)
    Page<ProductDocument> findByCategoryAndManufacturerAndPriceRange(
            String category,
            List<String> manufacturer,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );

    List<ProductDocument> findByProductIdIn(List<String> productIds);
}
