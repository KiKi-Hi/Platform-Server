package com.jiyoung.kikihi.platform.adapter.out.elasticSearch;

import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.util.List;

/**
 * 제품 정보를 담는 클래스입니다.
 * 배치서버에서 DB에 저장한 데이터을 담는 역할을 수행합니다.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "products")
@Mapping(mappingPath = "elasticSearch/dict/mapping.json")
@Setting(settingPath = "elasticSearch/dict/setting.json")
public class ProductESDocument {

    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String thumbnail;

    @Field(type = FieldType.Text)
    private String manufacturerName;

    @Field(type = FieldType.Text)
    private String productName;

    @Field(type = FieldType.Double)
    private double discountRate;

    @Field(type = FieldType.Double)
    private double discountedPrice;

    public ProductESDocument toESDocument(Product product) {
        double discountRate = 0; // 추후 할인 정책 적용
        double discountedPrice = product.getPrice(); // 추후 할인 반영

        return ProductESDocument.builder()
                .id(product.getId())
                .thumbnail(product.getThumbnail())
                .manufacturerName(product.getManufacturer())
                .productName(product.getName())
                .discountRate(discountRate)
                .discountedPrice(discountedPrice)
                .build();
    }


}
