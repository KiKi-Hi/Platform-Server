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

    @Field("categoryId")
    private Long categoryId; // 카테고리 아이디

    @Field("productName")
    private String productName; // 상품명

    @Field("seller")
    private String seller; // 판매처

    @Field("productThumbnail")
    private String productThumbnail; // 상품 썸네일

    @Field("productUrl")
    private String productUrl; // 상품 URL

    @Field("originalPrice")
    private String originalPrice; // 정가

    @Field("discountRate")
    private String discountRate; // 할인율

    @Field("discountedPrice")
    private String discountedPrice; // 할인가

    @Field("shippingCost")
    private String shippingCost; // 배송비

    @Field("rating")
    private String rating; // 별점

    @Field("reviewCount")
    private String reviewCount; // 리뷰 수

    @Field("stockQuantity")
    private String stockQuantity; // 재고 수량

    @Field("productInfo")
    private String productInfo; // 상품 정보

    @Field("relatedTags")
    private String relatedTags; // 관련 태그

    @Field("productDetailText")
    private String productDetailText; // 상품 상세 텍스트

    @Field("productDetailImages")
    private List<String> productDetailImages; // 상품 상세 이미지

    @CreatedDate
    @Field("createdAt")
    private String createdAt; // 생성일

    @LastModifiedDate
    @Field("updatedAt")
    private String updatedAt; // 수정일
}
