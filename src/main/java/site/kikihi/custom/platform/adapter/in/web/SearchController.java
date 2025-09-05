package site.kikihi.custom.platform.adapter.in.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.global.response.page.SliceResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.search.SearchListResponse;
import site.kikihi.custom.platform.adapter.in.web.swagger.SearchControllerSpec;
import site.kikihi.custom.platform.application.in.search.SearchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.kikihi.custom.platform.domain.search.Search;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerSpec {

    private final SearchUseCase service;

    /// 상품 검색
    @GetMapping
    public ApiResponse<SliceResponse<ProductListResponse>> searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam int page,
            @RequestParam int size,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        /// 유저가 없다면 null 저장
        UUID userId = principalDetails != null ? principalDetails.getId() : null;

        /// 서비스 호출
        Slice<ProductListResponse> products = service.searchProducts(keyword, page, size, userId);

        /// 서비스 호출(총 검색 결과 개수)
        long countByKeyword = service.countByKeyword(keyword);

        /// 응답
        return ApiResponse.ok(SliceResponse.from(products, countByKeyword));
    }

    /// 나의 최근 검색어 조회
    @GetMapping("/my")
    public ApiResponse<List<SearchListResponse>> getMySearches(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        /// 서비스 호출
        List<Search> responses = service.getMySearches(principalDetails.getId());

        /// 리턴
        return ApiResponse.ok(SearchListResponse.from(responses));

    }

    /// 특정 검색어 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSearch(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        /// 서비스 호출
        service.deleteMySearchKeyword(id, principalDetails.getId());

        /// 리턴
        return ApiResponse.deleted();
    }


    /// 모든 검색어 삭제
    @DeleteMapping()
    public ApiResponse<Void> deleteAllSearch(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        /// 서비스 호출
        service.deleteAllKeywords(principalDetails.getId());

        /// 리턴
        return ApiResponse.deleted();
    }

}
