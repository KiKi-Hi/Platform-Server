package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FrameMongoRepository extends MongoRepository<FrameDocument, String> {
//    @Query("{'materials': ?0, 'mountType': ?1, 'soundDampening': ?2, 'weight': ?3, 'layout': ?4}")
//    Page<FrameDocument> findByFilter(
//            String material,
//            String mountType,
//            String soundDampening,  // ✅ String으로 변경
//            Double weight,  // ✅ Double로 변경
//            String layout,
//            Pageable pageable
//    );

    // 모든 프레임을 페이징 처리하여 조회
    Page<FrameDocument> findAllBy(Pageable pageable);

    // 필터를 적용한 조회
    @Query("{'materials': ?0, 'mountType': ?1, 'soundDampening': ?2, 'weight': ?3, 'layout': ?4}")
    Page<FrameDocument> findByFilter(
            String material,
            String mountType,
            String soundDampening,
            Double weight,
            String layout,
            Pageable pageable
    );}