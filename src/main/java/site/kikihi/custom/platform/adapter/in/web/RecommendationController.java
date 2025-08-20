package site.kikihi.custom.platform.adapter.in.web;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.KeyboardRecommendationRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.KeyboardRecommendationResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.in.web.swagger.RecommendControllerSpec;
import site.kikihi.custom.platform.application.in.recommendation.RecommendationUseCase;
import site.kikihi.custom.platform.domain.product.Product;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
public class RecommendationController implements RecommendControllerSpec {

    private final RecommendationUseCase service;

    /**
     * 홈화면에서 추천하는 API 구현
     */
    /**
     * 상품 추천 API
     */
    @GetMapping("/home")
    public ApiResponse<List<ProductListResponse>> getProductRecommendation(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        /// 유저가 존재하면 넣기
        UUID userId = principalDetails != null ? principalDetails.getId() : null;

        // 북마크 인기순, 서비스 호출
        List<Product> recommendation = service.getProductsByRecommendation(userId);

        // 응답 주기
        return ApiResponse.ok(ProductListResponse.from(recommendation));
    }

    /**
     * 튜토리얼 키보드 추천 API
     */
    @PostMapping("/tutorial")
    public ApiResponse<List<KeyboardRecommendationResponse>> getTutorialKeyboardRecommendation(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody KeyboardRecommendationRequest request
    ) {

        // 유저가 존재하면 넣기
        UUID userId = principalDetails != null ? principalDetails.getId() : null;

        // 튜토리얼 키보드 추천 서비스 호출
        List<KeyboardRecommendationResponse> recommendation = service.getTutorialKeyboardRecommendation(userId,request);

        // 응답 주기
        return ApiResponse.ok(recommendation);
    }


    /**
     * 유사한 상품 추천
     */
    @GetMapping("/{productId}")
    public ApiResponse<List<KeyboardRecommendationResponse>> getSimilarProducts(
            @PathVariable("productId") String productId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        // 유저가 존재하면 넣기
        UUID userId = principalDetails != null ? principalDetails.getId() : null;

        // 유사한 상품 추천 서비스 호출
        List<Product> similarProducts = service.getSimilarProducts(userId,productId);

        // 응답 주기
        return ApiResponse.ok(KeyboardRecommendationResponse.from(similarProducts));
    }
}
