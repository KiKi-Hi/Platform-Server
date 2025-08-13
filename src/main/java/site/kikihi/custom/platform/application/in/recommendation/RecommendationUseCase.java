package site.kikihi.custom.platform.application.in.recommendation;

import site.kikihi.custom.platform.domain.product.Product;
import java.util.List;
import java.util.UUID;

public interface RecommendationUseCase {

    /// 상품 추천
    List<Product> getProductsByRecommendation(UUID userId);

}
