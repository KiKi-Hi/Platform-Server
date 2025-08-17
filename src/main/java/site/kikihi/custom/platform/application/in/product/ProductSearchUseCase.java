package site.kikihi.custom.platform.application.in.product;

import site.kikihi.custom.platform.domain.product.Product;
import java.util.List;

public interface ProductSearchUseCase {
    List<Product> searchProducts(String keyword, int page, int size, float minScore);
    List<Product> filterProducts(String keyword, String manufacturer, Double minPrice, Double maxPrice, int page, int size);
}
