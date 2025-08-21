package site.kikihi.custom.platform.adapter.in.web;

import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.in.web.swagger.SearchControllerSpec;
import site.kikihi.custom.platform.application.service.SearchService;
import site.kikihi.custom.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerSpec {

    private final SearchService searchService;

    // 상품 검색
    @GetMapping
    public ApiResponse<List<ProductListResponse>> searchProducts(
            @RequestParam("keyword") String keyword,
            PageRequest pageRequest,
            @RequestParam(defaultValue = "0.001") float minScore
    ) {

        /// 서비스 호출
        List<Product> productList = searchService.searchProducts(keyword, pageRequest.getPage(), pageRequest.getSize(), minScore);

        /// DTO 수정
        List<ProductListResponse> responses = ProductListResponse.from(productList);

        /// 응답
        return ApiResponse.ok(responses);
    }

}
