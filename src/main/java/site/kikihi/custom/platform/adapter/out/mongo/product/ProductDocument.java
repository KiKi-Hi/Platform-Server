package site.kikihi.custom.platform.adapter.out.mongo.product;

import site.kikihi.custom.platform.domain.product.Product;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

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

    private String category;

    private String name;

    private double price;

    private List<String> description;

    @Field("thumbnail")
    private String thumbnail;

    private String manufacturer;

    @Field("detail_purchase_url")
    private String detailPageUrl;

    @Field("options")
    private List<Map<String, Object>> options;

    @Field("all_detail_images")
    private List<String> allDetailImages;

    @Field("spec_table")
    @Builder.Default
    private Map<String, Object> specTable = new java.util.HashMap<>();

    @Field("type")
    private String type;

    @Field("is_custom")
    private boolean isCustom;

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
                .specTable(specTable)
                .build();
    }
}

