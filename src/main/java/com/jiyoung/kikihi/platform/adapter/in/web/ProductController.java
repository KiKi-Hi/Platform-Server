package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.page.PageRequest;
import com.jiyoung.kikihi.global.response.page.PageResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.ProductControllerSpec;
import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 상품 관련 기능을 담당하는 API 입니다.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductUseCase productService;

    /// 상품 목록 조회
    @GetMapping()
    public ApiResponse<List<ProductListResponse>> getProducts() {

        // 서비스
        List<Product> products = productService.getProducts();

        // DTO 변환
        List<ProductListResponse> responses = ProductListResponse.from(products);

        return ApiResponse.ok(responses);
    }

    @GetMapping("/list")
    public ApiResponse<PageResponse<ProductListResponse>> getProductList(PageRequest pageRequest) {

        /// Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        /// 서비스
        Page<Product> products = productService.getProducts(pageable);

        /// 추출
        List<Product> productList = products.getContent();

        /// DTO 변환
        List<ProductListResponse> responses = ProductListResponse.from(productList);

        return ApiResponse.ok(new PageResponse<>(responses, pageRequest, responses.size()));
    }

}
