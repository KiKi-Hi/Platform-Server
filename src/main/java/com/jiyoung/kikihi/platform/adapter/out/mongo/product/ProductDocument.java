package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * 제품 상세 스펙 정보를 담는 클래스입니다.
 * MongoDB의 Embedded Document 용도로 사용됩니다.
 */

@Document(collection = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocument {

    @Id
    private String id;

    private String name;

    private double price;

    private List<String> description;

    private String thumbnail;

    @Field("detail_purchase_url")
    private String detailPageUrl;

    @Field("actual_purchase_url")
    private String actualPurchaseUrl;

    @Field("final_purchase_url")
    private String finalPurchaseUrl;

    private List<String> options;

    @Field("all_detail_images")
    private List<String> allDetailImages;

    @Field("spec_table")
    private ProductSpecDocument specTable;

    /// FROM
    public static ProductDocument from(Product product) {
        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDetailImageList())
                .thumbnail(product.getThumbnail())
                .detailPageUrl(product.getDetailPageUrl())
                .finalPurchaseUrl(product.getFinalPurchaseUrl())
                .options(product.getOptionList())
                .allDetailImages(product.getDetailImageList())
                .specTable(product.getSpecList())
                .build();
    }

    /// 도메인

}

