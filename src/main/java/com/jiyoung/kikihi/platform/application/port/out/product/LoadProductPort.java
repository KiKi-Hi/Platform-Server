package com.jiyoung.kikihi.platform.application.port.out.product;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.FrameFilter;
import com.jiyoung.kikihi.platform.adapter.out.mongo.product.FrameDocument;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.platform.domain.product.frame.Frame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoadProductPort {

    // 정렬은 최신,좋아요수(인기),
    // 프레임 전체 가져오기 (정렬, 필터링, 페이징)
    Page<Frame> getFrames(FrameFilter filter, Pageable pageable);
    // 스위치 전체 가져오기 (정렬, 필터링, 페이징)
    // 키캡 전체 가져오기 (정렬, 필터링, 페이징)
    Page<Frame> findAllFrames(Pageable pageable);

    // 상품 상세 정보 가져오기
    Optional<Product> loadProductById(Long productId);

//    // 조건에 따른 조회
//    Page<Product> loadProductsByCondition(Pageable pageable, Long categoryId, String productTitle, Double minPrice, Double maxPrice);
//
//    // 상품 재고 조회
//    Integer findStockByProductId(Long productId);


}
