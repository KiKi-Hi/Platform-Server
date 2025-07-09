package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.global.response.page.PageRequest;
import com.jiyoung.kikihi.global.response.page.SliceResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductDetailResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.ProductControllerSpec;
import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import com.jiyoung.kikihi.platform.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
public class ProductController implements ProductControllerSpec {

    private final ProductUseCase productService;

    /// 상품 목록 조회 API
    //  상품 목록 조회 & 필터링 - mongoDB
    @GetMapping("/list")
    public ApiResponse<SliceResponse<ProductListResponse>> getProductList(PageRequest pageRequest,
                                                                          @RequestParam String category,
                                                                          @RequestParam(required = false) List<String> manufacturer,
                                                                          @RequestParam(required = false) Integer minPrice,
                                                                          @RequestParam(required = false) Integer maxPrice) {

        /// Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        /// 파라미터에 따라서 분기
        Slice<Product> products;

        // 파라미터 여부에 따라 분기 처리
        if (manufacturer == null && minPrice == null && maxPrice == null) {
            /// 카테고리만 있는 경우
            products = productService.getProductsByCategoryId(category, pageable);

        } else if (manufacturer != null && minPrice == null && maxPrice == null) {
            /// 카테고리 + 제조사만 있는 경우
            products = productService.getProductsByCategoryIdAndManufacturerId(category, manufacturer, pageable);

        } else if (manufacturer == null && minPrice != null && maxPrice != null) {
            /// 카테고리 + 가격만 있는 경우
            products = productService.getProductsByCategoryIdAndPrice(category, minPrice, maxPrice, pageable);

        } else if (manufacturer != null && minPrice != null && maxPrice != null) {
            /// 카테고리 + 제조사 + 가격이 있는 경우
            products = productService.getProductsByCategoryIdAndManufacturerIdAndPrice(
                    category, manufacturer, minPrice, maxPrice, pageable);
        }  else {
            throw new IllegalArgumentException(ErrorCode.BAD_REQUEST.getMessage());
        }

        /// 추출
        List<Product> productList = products.getContent();

        /// DTO 변환
        List<ProductListResponse> responses = ProductListResponse.from(productList);

        /// 슬라이싱 재생성
        SliceImpl<ProductListResponse> slice = new SliceImpl<>(responses, pageable, products.hasNext());

        return ApiResponse.ok(SliceResponse.from(slice));
    }

    /// 상품 상세 조회 API
    @GetMapping
    public ApiResponse<ProductDetailResponse> getProduct(
            @RequestParam String id) {

        // 서비스
        Product product = productService.getProduct(id);

        // DTO 변환
        ProductDetailResponse response = ProductDetailResponse.from(product);

        return ApiResponse.ok(response);
    }

}

