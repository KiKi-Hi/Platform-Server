package site.kikihi.custom.platform.domain.product;

import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocument;

import java.util.Arrays;
import java.util.UUID;

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
                .id(UUID.randomUUID().toString())
                .category("test")
                .name("테스트 전자제품")
                .price(129000.0)
                .description(Arrays.asList("고성능", "에너지 절약", "심플 디자인"))
                .thumbnail("https://example.com/images/prd-001-thumb.jpg")
                .detailPageUrl("https://example.com/products/prd-001/detail")
                .options(Arrays.asList("화이트", "블랙", "실버"))
                .allDetailImages(Arrays.asList(
                        "https://example.com/images/prd-001-1.jpg",
                        "https://example.com/images/prd-001-2.jpg"
                ))
                .manufacturer("테스트전자")
                .build();
    }

    public static ProductDocument createProduct(String category, String name,double price) {
        return ProductDocument.builder()
                .id(UUID.randomUUID().toString())
                .category(category)
                .name(name)
                .price(price)
                .description(Arrays.asList("고성능", "에너지 절약", "심플 디자인"))
                .thumbnail("https://example.com/images/prd-001-thumb.jpg")
                .detailPageUrl("https://example.com/products/prd-001/detail")
                .options(Arrays.asList("화이트", "블랙", "실버"))
                .allDetailImages(Arrays.asList(
                        "https://example.com/images/prd-001-1.jpg",
                        "https://example.com/images/prd-001-2.jpg"
                ))
                .manufacturer("테스트전자")
                .build();
    }

    public static ProductDocument createProduct(String category, String name, String manufacturer, double price) {

        return ProductDocument.builder()
                .id(UUID.randomUUID().toString())
                .category(category)
                .name(name)
                .price(price)
                .description(Arrays.asList("고성능", "에너지 절약", "심플 디자인"))
                .thumbnail("https://example.com/images/prd-001-thumb.jpg")
                .detailPageUrl("https://example.com/products/prd-001/detail")
                .options(Arrays.asList("화이트", "블랙", "실버"))
                .allDetailImages(Arrays.asList(
                        "https://example.com/images/prd-001-1.jpg",
                        "https://example.com/images/prd-001-2.jpg"
                ))
                .manufacturer(manufacturer)
                .build();
    }


}
