package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.RecommendControllerSpec;
import com.jiyoung.kikihi.platform.application.in.recommendation.RecommendationUseCase;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping("/recommendation")
    public ApiResponse<List<ProductListResponse>> getProductRecommendation() {

        // 북마크 인기순, 서비스 호출
        List<Product> recommendation = service.getProductsByRecommendation();

        // 응답 주기
        return ApiResponse.ok(ProductListResponse.from(recommendation));
    }



}
