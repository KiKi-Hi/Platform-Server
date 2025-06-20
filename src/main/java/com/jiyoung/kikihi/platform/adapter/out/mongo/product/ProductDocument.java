package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * 제품 정보를 담는 클래스입니다.
 * 배치서버에서 DB에 저장한 데이터을 담는 역할을 수행합니다.
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
    private ProductSpecDocument spec;

    /// 도메인 변경
    public Product toDomain(){
        return Product.builder()
                .id(id)
                .name(name)
                .manufacturer(spec.getManufacturer())
                .price(price)
                .description(description)
                .thumbnail(thumbnail)
                .detailPageUrl(detailPageUrl)
                .actualPurchaseUrl(actualPurchaseUrl)
                .finalPurchaseUrl(finalPurchaseUrl)
                .options(options)
                .allDetailImages(allDetailImages)
                .build();
    }
}

