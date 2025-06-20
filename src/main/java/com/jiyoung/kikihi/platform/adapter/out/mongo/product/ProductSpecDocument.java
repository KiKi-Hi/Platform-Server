package com.jiyoung.kikihi.platform.adapter.out.mongo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 제품 상세 스펙 정보를 담는 클래스입니다.
 * MongoDB의 Embedded Document 용도로 사용됩니다.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecDocument {

    @Field("제조회사")
    private String manufacturer;

    @Field("등록년월")
    private String releaseDate;

}
