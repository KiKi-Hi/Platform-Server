package site.kikihi.custom.platform.adapter.out.elasticSearch;

import site.kikihi.custom.platform.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
public class ProductESDocument {

    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;  //

    @Field(type = FieldType.Keyword)
    private String category;    //

    @Field(type = FieldType.Text)
    private String name;    //

    @Field(type = FieldType.Double)
    private double price;   //

    @Field(type = FieldType.Text)
    private List<String> description;   //

    @Field(type = FieldType.Text)
    private String thumbnailUrl;

    @Field(type = FieldType.Keyword)
    private String manufacturer;    //

    @Field(type = FieldType.Text)
    private String detailPageUrl;   //

    @Field(type = FieldType.Object)
    private List<Map<String, Object>> options;   //

    @Field(type = FieldType.Object)
    private Map<String, Object> specTable;  //

    @Field(type = FieldType.Text)
    private List<String> allDetailImages;   //


    /// 정적 팩토리 메서드
    public static ProductESDocument from(Product product) {

        // Product 도메인에 추가 필드가 있다면 여기서 매핑 필요
        return ProductESDocument.builder()
                .id(product.getId())
                .category(product.getCategory())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .thumbnailUrl(product.getThumbnail())
                .manufacturer(product.getManufacturer())
                .detailPageUrl(product.getDetailPageUrl())
                .options(product.getOptions())
                .specTable(product.getSpecTable())
                .allDetailImages(product.getAllDetailImages())
                .build();
    }

    /// 정적 팩토리 메서드
    public Product toDomain() {
        return Product.builder()
                .id(id)
                .name(name)
                .category(category)
                .price(price)
                .description(description)
                .thumbnail(thumbnailUrl)
                .manufacturer(manufacturer)
                .detailPageUrl(detailPageUrl)
                .options(options)
                .specTable(specTable)
                .allDetailImages(allDetailImages)
                .build();
    }
}

