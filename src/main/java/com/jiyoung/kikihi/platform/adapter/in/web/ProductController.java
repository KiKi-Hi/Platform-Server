package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.page.PageRequest;
import com.jiyoung.kikihi.global.response.page.PageResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.platform.adapter.out.elasticSearch.ProductESDocument;
import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    //  1. 상품 목록 조회 & 필터링 - mongoDB
    @GetMapping("/list")
    public ApiResponse<PageResponse<ProductListResponse>> getProductList(PageRequest pageRequest,
                                                                         @RequestParam(required = false) String manufacturer,
                                                                         @RequestParam(required = false) Integer minPrice,
                                                                         @RequestParam(required = false) Integer maxPrice) {

        /// Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        /// 서비스
        Page<Product> products = productService.getProducts(pageable, manufacturer, minPrice, maxPrice);

        /// 추출
        List<Product> productList = products.getContent();

        /// DTO 변환
        List<ProductListResponse> responses = ProductListResponse.from(productList);

        return ApiResponse.ok(new PageResponse<>(responses, pageRequest, responses.size()));
    }

    /// 2. 상품 목록 조회 (페이징 X)
    @GetMapping()
    public ApiResponse<List<ProductListResponse>> getProducts() {

        // 서비스
        List<Product> products = productService.getProducts();

        // DTO 변환
        List<ProductListResponse> responses = ProductListResponse.from(products);

        return ApiResponse.ok(responses);
    }


    // 3. 상품 상세 조회 (상품 이미지 (최대 5개 - 캐러셀 형식), 제조사, 제품명, 할인율, 정가, 할인가, 배송 정보(배송비, 배송 종류, 배송 날짜), 어울리는 상품 추천, 상품 유의사항, 상세 정보 이미지)

    // 4. 상품 옵션 조회


}

