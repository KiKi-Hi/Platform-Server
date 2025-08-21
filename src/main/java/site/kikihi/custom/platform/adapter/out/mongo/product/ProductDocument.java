package site.kikihi.custom.platform.adapter.out.mongo.product;

import site.kikihi.custom.platform.domain.product.Product;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
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
    private String id;  //

    private String category;    //

    private String name;    //

    private double price;   //

    private List<String> description;   //

    @Field("thumbnail")
    private String thumbnail;   //

    private String manufacturer;    //

    @Field("detail_purchase_url")
    private String detailPageUrl;   //

    @Field("options")
    private List<Object> options;   //

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
                .options(convertOptions())
                .allDetailImages(allDetailImages)
                .build();
    }

    /**
     * MongoDB에서 가져온 옵션을 List<Map<String, Object>> 형태로 안전하게 변환
     */
    private List<Map<String, Object>> convertOptions() {

        /// 결과 리턴할 리스트 생성
        List<Map<String, Object>> result = new ArrayList<>();

        /// 예외처리
        if (options == null) {
            return result;}

        /// 반복문
        for (Object o : options) {
            if (o instanceof String) {
                /// 문자열 옵션 -> Map으로 변환
                Map<String, Object> map = new HashMap<>();
                map.put("option_name", o);
                result.add(map);
            } else if (o instanceof Map) {
                /// Object 옵션 그대로 변환
                result.add((Map<String, Object>) o);
            }
        }
        return result;
    }
}

