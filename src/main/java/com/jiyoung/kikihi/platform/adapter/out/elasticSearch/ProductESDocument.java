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
import java.util.Map;

/**
 * 제품 정보를 담는 클래스입니다.
 * 배치서버에서 DB에 저장한 데이터을 담는 역할을 수행합니다.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "products")
@Mapping(mappingPath = "elasticSearch/mapping.json")
@Setting(settingPath = "elasticSearch/setting.json")
public class ProductESDocument {

    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String thumbnail;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Keyword)
    private String manufacturer;

    @Field(type = FieldType.Text)
    private List<String> description;

    @Field(type = FieldType.Text)
    private String finalPurchaseUrl;

    @Field(type = FieldType.Double)
    private double discountRate;

    @Field(type = FieldType.Double)
    private double discountedPrice;

    // 추가: JSON 데이터에 맞는 필드들
    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private List<String> options;

    @Field(type = FieldType.Object)
    private Map<String, Object> specTable; // spec_table

    @Field(type = FieldType.Text)
    private List<String> allDetailImages; // all_detail_images

    @Field(type = FieldType.Text)
    private String detailPageUrl; // detail_page_url

    public static ProductESDocument toESDocument(Product product) {
        double discountRate = 0; // 추후 할인 정책 적용
        double discountedPrice = product.getPrice(); // 추후 할인 반영

        // Product 도메인에 추가 필드가 있다면 여기서 매핑 필요
        return ProductESDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .thumbnail(product.getThumbnail())
                .price(product.getPrice())
                .manufacturer(product.getManufacturer())
                .description(product.getDescription())
                .finalPurchaseUrl(product.getFinalPurchaseUrl())
                .discountRate(discountRate)
                .discountedPrice(discountedPrice)
                // 아래는 Product 도메인에 추가해야 함 (예시)
                .category(product.getCategory())
                .options(product.getOptions())
                .specTable(product.getSpecTable())
                .allDetailImages(product.getAllDetailImages())
                .detailPageUrl(product.getDetailPageUrl())
                .build();
    }
}

