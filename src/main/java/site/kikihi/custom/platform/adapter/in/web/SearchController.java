package site.kikihi.custom.platform.adapter.in.web;

import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.application.service.ElasticSearchService;
import site.kikihi.custom.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final ElasticSearchService searchService;

    // 상품 검색
    @GetMapping
    public ApiResponse<List<ProductListResponse>> searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0.001") float minScore
    ) {

        List<Product> productList = searchService.searchProducts(keyword, page, size, minScore);
        List<ProductListResponse> responses = ProductListResponse.from(productList);

        return ApiResponse.ok(responses);
    }

    // 상품 필터링
//    @GetMapping("/filter")
//    public ApiResponse<List<Product>> filterProducts(
//            @RequestParam("keyword") String keyword,
//            @RequestBody SearchRequest req,
//            PageRequest pageRequest
//    ) {
//        List<Product> products = searchService.filterProducts(keyword, req.manufacturer(), req.minPrice(), req.maxPrice(), pageRequest.getPage(), pageRequest.getSize());
//        return ApiResponse.ok(products);
//    }
}
