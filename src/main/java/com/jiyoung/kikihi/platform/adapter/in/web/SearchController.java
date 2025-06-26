package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.platform.application.service.ElasticSearchService;
import com.jiyoung.kikihi.platform.domain.product.Product;
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
        System.out.println("✨"+productList);
        List<ProductListResponse> responses = ProductListResponse.from(productList);
        System.out.println("✨"+responses);

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
