package com.jiyoung.kikihi.platform.application.port.in.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetPartsUseCase {

    /*
        사용자가 키보드에 대한 정보를 얻는 유즈케이스
        관심 목록이 제일 위에 가도록 설정한다.
        필터링은 가격, 브랜드 , 색상으로 가능함
        각각 조립품(하우징, 키캡, 스위치 등)은 각 사이트의 구매 링크를 모아서 사용자가 한번에 결제하도록 만들 수 있음
     */


    // 상품 상세 조회
    Product getProduct(Long id);

    // 상품 최신순 (카테고리별 조회)
    Page<Product> getProducts(Pageable pageable, Long categoryId);

    // 상품 인기순 (카테고리별 조회)
    Page<Product> getProductsByLike(Pageable pageable, Long categoryId);

    // 상품 조건별 , 조건문 추가해야 한다. (카테고리별 조회)
    Page<Product> getProductsByCondition(Pageable pageable, Long categoryId);

    // 상품 관심목록 (카테고리별 조회)
    Page<Product> getProductsByWishList(Pageable pageable, Long categoryId);


}
