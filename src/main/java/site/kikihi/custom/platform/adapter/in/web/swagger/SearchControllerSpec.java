package site.kikihi.custom.platform.adapter.in.web.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.global.response.page.SliceResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.search.SearchListResponse;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;

import java.util.List;

@Tag(name = "검색 API", description = "검색을 위한 API 입니다.")
public interface SearchControllerSpec {

    @Operation(
            summary = "검색 API",
            description = "키워드를 바탕으로 조회합니다."
    )
    ApiResponse<SliceResponse<ProductListResponse>> searchProducts(
            @Parameter(example = "하우징")
            @RequestParam("keyword") String keyword,
            PageRequest pageRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );


    @Operation(
            summary = "나의 최근 검색어 목록 API",
            description = "JWT를 바탕으로 나의 최근 검색어를 조회합니다."
    )
    ApiResponse<List<SearchListResponse>> getMySearches(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(
            summary = "특정 검색어 삭제 API",
            description = "JWT를 바탕으로 특정 검색어를 삭제합니다."
    )
    ApiResponse<Void> deleteSearch(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );




    @Operation(
            summary = "모든 검색어 삭제 API",
            description = "JWT를 바탕으로 모든 검색어를 삭제합니다."
    )
    ApiResponse<Void> deleteAllSearch(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

}
