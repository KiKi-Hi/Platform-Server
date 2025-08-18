package site.kikihi.custom.platform.adapter.in.web.swagger;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.KeyboardRecommendationRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.KeyboardRecommendationResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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
    ApiResponse<List<ProductListResponse>> getProductRecommendation(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    /**
     * 튜토리얼 키보드 추천 API
     */
    @Operation(
            summary = "추천 키보드 리스트",
            description = "튜토리얼에서 키보드 추천 리스트를 불러오는 API 입니다."
            )
    ApiResponse<List<KeyboardRecommendationResponse>> getTutorialKeyboardRecommendation(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody KeyboardRecommendationRequest request
    );


}
