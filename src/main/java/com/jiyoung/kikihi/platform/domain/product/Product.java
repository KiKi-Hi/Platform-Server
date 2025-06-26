package com.jiyoung.kikihi.platform.domain.product;

import com.jiyoung.kikihi.platform.adapter.out.elasticSearch.ProductESDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private String id;

    private String name;

    private String category;

    private double price;

    private List<String> description;

    private String thumbnail;

    private String manufacturer;

    private String detailPageUrl;

    private String actualPurchaseUrl;

    private String finalPurchaseUrl;

    private List<String> options;

    private Map<String, Object> specTable; // spec_table

    private List<String> allDetailImages;

    // Document → 도메인 변환
    public static Product toDomain(ProductESDocument doc) {
        return Product.builder()
                .id(doc.getId())
                .name(doc.getName())
                .price(doc.getDiscountedPrice())
                .description(doc.getDescription())
                .thumbnail(doc.getThumbnail())
                .manufacturer(doc.getManufacturer())
                .finalPurchaseUrl(doc.getFinalPurchaseUrl())
                .build();
    }

}
