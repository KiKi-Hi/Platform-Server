package com.jiyoung.kikihi.platform.application.in.product;

import com.jiyoung.kikihi.platform.domain.product.Product;
import java.util.List;

public interface ProductSearchUseCase {
    List<Product> searchProducts(String keyword, int page, int size, float minScore);
    List<Product> filterProducts(String keyword, String manufacturer, Double minPrice, Double maxPrice, int page, int size);
}
