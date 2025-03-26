package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import com.jiyoung.kikihi.platform.domain.product.frame.Frame;
import com.jiyoung.kikihi.platform.domain.product.frame.Layout;
import com.jiyoung.kikihi.platform.domain.product.frame.Material;
import com.jiyoung.kikihi.platform.domain.product.frame.MountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "frame")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FrameDocument extends ProductDocument {

    @Field("layout")
    private Layout layout;          // 키보드 레이아웃 (예: FULL, TKL 등)

    @Field("materials")
    private Material materials;     // 재질 (예: ALUMINUM, PLASTIC 등)

    @Field("mountType")
    private MountType mountType;    // 장착 방식 (예: GASKET, TRAY 등)

    @Field("weight")
    private double weight;          // 무게 (예: 1.5)

    @Field("soundDampening")
    private String soundDampening;  // 흡음 (예: "폼 추가")

    public Frame toDomain() {
        return Frame.builder()
                .id(this.getId())
                .categoryId(this.getCategoryId())
                .productName(this.getProductName())
                .seller(this.getSeller())
                .productThumbnail(this.getProductThumbnail())
                .productUrl(this.getProductUrl())
                .originalPrice(this.getOriginalPrice())
                .discountRate(this.getDiscountRate())
                .discountedPrice(this.getDiscountedPrice())
                .shippingCost(this.getShippingCost())
                .rating(this.getRating())
                .reviewCount(this.getReviewCount())
                .stockQuantity(this.getStockQuantity())
                .productInfo(this.getProductInfo())
                .relatedTags(this.getRelatedTags())
                .productDetailText(this.getProductDetailText())
                .productDetailImages(this.getProductDetailImages())
                .layout(this.layout)
                .materials(this.materials)
                .mountType(this.mountType)
                .weight(this.weight)
                .soundDampening(this.soundDampening)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
