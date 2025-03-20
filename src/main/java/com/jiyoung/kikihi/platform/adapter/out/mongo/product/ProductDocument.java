package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "product") // MongoDB 컬렉션 이름
@Builder
@Getter
public class ProductDocument {

    @Id
    private Long id;

    private Long categoryId;

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

    // from
    public static ProductDocument from (Product product) {
        return ProductDocument.builder()
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .sortBy(product.getSortBy())
                .productName(product.getProductName())
                .seller(product.getSeller())
                .productThumbnail(product.getProductThumbnail())
                .productUrl(product.getProductUrl())
                .originalPrice(product.getOriginalPrice())
                .discountRate(product.getDiscountRate())
                .discountedPrice(product.getDiscountedPrice())
                .shippingCost(product.getShippingCost())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .stockQuantity(product.getStockQuantity())
                .productInfo(product.getProductInfo())
                .relatedTags(product.getRelatedTags())
                .productDetailText(product.getProductDetailText())
                .productDetailImages(product.getProductDetailImages())
                .productInfoDisclosure(product.getProductInfoDisclosure())
                .transactionConditions(product.getTransactionConditions())
                .build();
    }

    // toDomain
    public Product toDomain() {
        return Product.builder()
                .id(id)
                .categoryId(categoryId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .sortBy(sortBy)
                .productName(productName)
                .seller(seller)
                .productThumbnail(productThumbnail)
                .productUrl(productUrl)
                .originalPrice(originalPrice)
                .discountRate(discountRate)
                .discountedPrice(discountedPrice)
                .shippingCost(shippingCost)
                .rating(rating)
                .reviewCount(reviewCount)
                .stockQuantity(stockQuantity)
                .productInfo(productInfo)
                .relatedTags(relatedTags)
                .productDetailText(productDetailText)
                .productDetailImages(productDetailImages)
                .productInfoDisclosure(productInfoDisclosure)
                .transactionConditions(transactionConditions)
                .build();

    }

}
