package site.kikihi.custom.platform.adapter.in.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.search.SearchListResponse;
import site.kikihi.custom.platform.adapter.in.web.swagger.SearchControllerSpec;
import site.kikihi.custom.platform.application.in.search.SearchUseCase;
import site.kikihi.custom.platform.domain.product.Product;
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
    public ApiResponse<List<ProductListResponse>> searchProducts(
            @RequestParam("keyword") String keyword,
            PageRequest pageRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        /// 유저가 없다면 null 저장
        UUID userId = principalDetails != null ? principalDetails.getId() : null;

        /// 서비스 호출
        List<Product> productList = service.searchProducts(keyword, pageRequest.getPage(), pageRequest.getSize(), userId);

        /// DTO 수정
        List<ProductListResponse> responses = ProductListResponse.from(productList);

        /// 응답
        return ApiResponse.ok(responses);
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

    /// 자동 저장 기능 조회
    @GetMapping("/auto")
    public ApiResponse<String> getMyAutoSearch(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        /// 서비스 호출
        boolean checked = service.checkSearch(principalDetails.getId());

        String autoSearch = checked ? "자동 저장이 활성화되었습니다." : "자동 저장이 꺼져있습니다.";

        /// 리턴
        return ApiResponse.ok(autoSearch);

    }

    /// 자동 저장 기능 켜기
    @PutMapping("/auto/on")
    public ApiResponse<Void> turnOnSearch(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){

        /// 서비스 호출
        service.turnOnMySearchKeyword(principalDetails.getId());

        /// 리턴
        return ApiResponse.updated();

    }

    /// 자동 저장 기능 끄기
    @PutMapping("/auto/off")
    public ApiResponse<Void> turnOffSearch(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){

        /// 서비스 호출
        service.turnOffMySearchKeyword(principalDetails.getId());

        /// 리턴
        return ApiResponse.updated();
    }

}
