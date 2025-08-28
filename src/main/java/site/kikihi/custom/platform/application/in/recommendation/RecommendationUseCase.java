package site.kikihi.custom.platform.application.in.recommendation;

import site.kikihi.custom.platform.adapter.in.web.dto.request.product.KeyboardRecommendationRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.KeyboardRecommendationResponse;
import site.kikihi.custom.platform.domain.product.Product;
import java.util.List;
import java.util.UUID;

public interface RecommendationUseCase {

    /// 상품 추천
    List<Product> getProductsByRecommendation(UUID userId);

    /// 튜토리얼 키보드 추천
    List<KeyboardRecommendationResponse> getTutorialKeyboardRecommendation(UUID userId, KeyboardRecommendationRequest request);

    /// 유사한 상품 추천
    List<KeyboardRecommendationResponse> getSimilarProducts(UUID userId, String productId);

}
