package com.jiyoung.kikihi.platform.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public abstract class Product {

    private String id;
    private Long categoryId; // 카테고리 아이디를 통해서 분류한다.

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

    private String createdAt;
    private String updatedAt;

}
