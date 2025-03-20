package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "base_product") // MongoDB 컬렉션 이름
public abstract class BaseProductDocument {

    @Id
    private Long id;
    private String createdAt;
    private String updatedAt;
    private String sortBy; // 정렬기준
    private String productName; // 상품명
    private String seller; // 판매처
    private String productThumbnail; // 상품 썸네일
    private String productUrl; // 상품 url
    private String originalPrice; // 정가
    private String discountRate; // 할인율
    private String discountedPrice; // 할인가
    private String shippingCost; // 배송비
    private String rating; // 별점
    private String reviewCount; // 리뷰수
    private String stockQuantity; // 재고 수량
    private String productInfo; // 상품정보
    private String relatedTags; // 관련 태그
    private String productDetailText; // 상품 상세 텍스트
    private List<String> productDetailImages; // 상품 상세 이미지
    private String productInfoDisclosure; // 상품정보 제공공시
    private String transactionConditions; // 거래조건에 관한 정보

}
