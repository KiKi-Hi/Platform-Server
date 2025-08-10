package com.jiyoung.kikihi.platform.application.in.recommendation;

import com.jiyoung.kikihi.platform.domain.product.Product;
import java.util.List;

public interface RecommendationUseCase {

    /// 상품 추천
    List<Product> getProductsByRecommendation();

}
