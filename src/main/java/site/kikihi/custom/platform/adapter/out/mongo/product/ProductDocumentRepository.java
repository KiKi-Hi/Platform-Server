package site.kikihi.custom.platform.adapter.out.mongo.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ProductDocumentRepository extends MongoRepository<ProductDocument, String> {

    // =================
    //  생성 함수
    // =================


    // =================
    //  조회 함수
    // =================

    /// 카테고리 기반 목록 조회 (카테고리)
    Slice<ProductDocument> findByCategory(String category, Pageable pageable);

    /// 카테고리 기반 목록 조회 (카테고리, 제조사 포함)
    @Query("{ 'category': ?0, 'manufacturer': { $in: ?1 } }")
    Page<ProductDocument> findByCategoryAndManufacturer(String category, List<String> manufacturer, Pageable pageable);

    /// 카테고리 기반 목록 조회 (카테고리, 가격 포함)
    @Query("""
    {
      'category': ?0,
      'price': { $gte: ?1, $lte: ?2 }
    }
    """)
    Slice<ProductDocument> findByCategoryAndPriceRange(
            String category,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );

    /// 카테고리 기반 목록 조회 (카테고리, 제조사, 가격 포함)
    @Query("""
    {
      'category': ?0,
      'manufacturer': { $in: ?1 },
      'price': { $gte: ?2, $lte: ?3 }
    }
    """)
    Slice<ProductDocument> findByCategoryAndManufacturerAndPriceRange(
            String category,
            List<String> manufacturer,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );


    @Query("""
    {
      'type': ?0,
      'category': ?1,
      'is_custom': true
    }
    """)
    Slice<ProductDocument> findByTypeAndCategoryAndIsCustomTrue(
            String type,
            String category,
            Pageable pageable
    );

    @Query("""
    {
      'category': ?0,
      'is_custom': true
    }
    """)
    Slice<ProductDocument> findByCategoryAndIsCustomTrue(
            String category,
            Pageable pageable
    );

    /// 아이디 기반 조회
    Slice<ProductDocument> findByIdIn(List<String> ids, Pageable pageable);

    /// 한번에 상품 여러개 조회
    List<ProductDocument> findByIdIn(Collection<String> ids);

    /// 랜덤 기반 조회
    @Aggregation(pipeline = {
            "{ $sample: { size: ?0 } }"
    })
    List<ProductDocument> findRandomProducts(int limit);

    /// 특정아이디 제외한, 랜덤 기반 조회
    @Aggregation(pipeline = {
            "{ $match: { _id: { $nin: ?0 } } }",
            "{ $sample: { size: ?1 } }"
    })
    List<ProductDocument> findRandomExcludeIds(List<String> excludedIds, int limit);

    // =================
    //  삭제 함수
    // =================
}
