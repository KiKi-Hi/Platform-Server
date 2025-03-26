package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
public abstract class ProductDocument {

    @Id
    @Field("_id")
    private String id; // JSON의 _id는 문자열입니다.

    private Long categoryId; // 카테고리 아이디

    private String productName; // 상품명
    private String seller; // 판매처
    private String productThumbnail; // 상품 썸네일
    private String productUrl; // 상품 URL

    private String originalPrice; // 정가
    private String discountRate; // 할인율
    private String discountedPrice; // 할인가
    private String shippingCost; // 배송비

    private String rating; // 별점
    private String reviewCount; // 리뷰 수
    private String stockQuantity; // 재고 수량

    private String productInfo; // 상품 정보
    private String relatedTags; // 관련 태그

    private String productDetailText; // 상품 상세 텍스트
    private List<String> productDetailImages; // 상품 상세 이미지

    @CreatedDate
    private String createdAt; // 생성일

    @LastModifiedDate
    private String updatedAt; // 수정일
}
