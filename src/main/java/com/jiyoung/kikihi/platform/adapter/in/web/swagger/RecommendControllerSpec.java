package com.jiyoung.kikihi.platform.adapter.in.web.swagger;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "추천 API", description = "홈화면에서 사용되는 추천 API")
public interface RecommendControllerSpec {

    /**
     * 홈화면에서 인기 상품 조회하기
     */
    @Operation(
            summary = "인기상품 API_홈",
            description = "북마크를 바탕으로 8개의 인기 상품을 불러오는 API 입니다."

    )
    ApiResponse<List<ProductListResponse>> getProductRecommendation();


}
