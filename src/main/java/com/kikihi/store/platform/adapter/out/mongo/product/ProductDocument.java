package com.kikihi.store.platform.adapter.out.mongo.product;

import com.kikihi.store.platform.domain.product.Product;
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
    private String id;  //

    private String category;    //

    private String name;    //

    private double price;   //

    private List<String> description;   //

    private String thumbnail;   //

    @Field("spec_table.제조회사")
    private String manufacturer;    //

    @Field("detail_purchase_url")
    private String detailPageUrl;   //

    private List<String> options;   //

    @Field("all_detail_images")
    private List<String> allDetailImages;   //

    /// 도메인 변경
    public Product toDomain(){
        return Product.builder()
                .id(id)
                .category(category)
                .name(name)
                .price(price)
                .description(description)
                .thumbnail(thumbnail)
                .manufacturer(manufacturer)
                .detailPageUrl(detailPageUrl)
                .options(options)
                .allDetailImages(allDetailImages)
                .build();
    }
}

