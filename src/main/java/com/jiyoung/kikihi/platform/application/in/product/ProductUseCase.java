package com.jiyoung.kikihi.platform.application.in.product;

import com.jiyoung.kikihi.platform.domain.product.Product;

import java.util.*;

/**
 * 제품의 조회 기능을 담당하는 유즈 케이스입니다.
 * 상품 검색 - 상품 검색 키워드를 입력하고, 검색된 결과를 보여준다.
 * 상품 목록 조회 - 무한 스크롤 방식으로 진행하여, 한번에 40개씩 불러온다.
 * 상품 목록 조회 - [썸네일, 제조사명, 제품명, 할인율, 할인가, 나의 좋아요 여부]를 보여준다.
 * 상품 목록 조회 - 체크박스로 여러 개의 제조사 상품을 조회할 수 있다.
ㅌㅈ * 상품 상세 조회 - [ 상품 이미지 (최대 5개 - 캐러셀 형식), 제조사, 제품명, 할인율, 정가, 할인가, 배송 정보(배송비, 배송 종류, 배송 날짜), 어울리는 상품 추천, 상품 유의사항, 상세 정보 이미지] 의 정보를 제공한다.
 */

public interface ProductUseCase {

    List<Product> search(String keyword);


}
