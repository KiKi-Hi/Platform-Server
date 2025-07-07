package com.jiyoung.kikihi.platform.domain.product;

import com.jiyoung.kikihi.platform.adapter.out.mongo.product.ProductDocument;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductFixtures {

    public static ProductDocument fakeProduct() {
        return ProductDocument.builder()
                .id(UUID.randomUUID().toString())
                .category("test")
                .name("테스트 전자제품")
                .price(129000.0)
                .description(Arrays.asList("고성능", "에너지 절약", "심플 디자인"))
                .thumbnail("https://example.com/images/prd-001-thumb.jpg")
                .detailPageUrl("https://example.com/products/prd-001/detail")
                .actualPurchaseUrl("https://example.com/products/prd-001/buy")
                .finalPurchaseUrl("https://example.com/products/prd-001/final")
                .options(Arrays.asList("화이트", "블랙", "실버"))
                .allDetailImages(Arrays.asList(
                        "https://example.com/images/prd-001-1.jpg",
                        "https://example.com/images/prd-001-2.jpg"
                ))
                .manufacturer("테스트전자")
                .build();
    }

    public static ProductDocument createProduct() {
        return ProductDocument.builder()
                .id("prd-001")
                .category("test")
                .name("테스트 전자제품")
                .price(129000.0)
                .description(Arrays.asList("고성능", "에너지 절약", "심플 디자인"))
                .thumbnail("https://example.com/images/prd-001-thumb.jpg")
                .detailPageUrl("https://example.com/products/prd-001/detail")
                .actualPurchaseUrl("https://example.com/products/prd-001/buy")
                .finalPurchaseUrl("https://example.com/products/prd-001/final")
                .options(Arrays.asList("화이트", "블랙", "실버"))
                .allDetailImages(Arrays.asList(
                        "https://example.com/images/prd-001-1.jpg",
                        "https://example.com/images/prd-001-2.jpg"
                ))
                .manufacturer("테스트전자")
                .build();
    }

    public static ProductDocument createProduct(String category, String name) {
        return ProductDocument.builder()
                .id("prd-001")
                .category(category)
                .name(name)
                .price(129000.0)
                .description(Arrays.asList("고성능", "에너지 절약", "심플 디자인"))
                .thumbnail("https://example.com/images/prd-001-thumb.jpg")
                .detailPageUrl("https://example.com/products/prd-001/detail")
                .actualPurchaseUrl("https://example.com/products/prd-001/buy")
                .finalPurchaseUrl("https://example.com/products/prd-001/final")
                .options(Arrays.asList("화이트", "블랙", "실버"))
                .allDetailImages(Arrays.asList(
                        "https://example.com/images/prd-001-1.jpg",
                        "https://example.com/images/prd-001-2.jpg"
                ))
                .manufacturer("테스트전자")
                .build();
    }


}
