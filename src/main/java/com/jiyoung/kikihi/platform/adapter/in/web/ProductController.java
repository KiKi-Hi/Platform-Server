package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.ErrorCode;
import com.jiyoung.kikihi.global.response.page.PageRequest;
import com.jiyoung.kikihi.global.response.page.SliceResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.product.CategoryType;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductDetailResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.ProductControllerSpec;
import com.jiyoung.kikihi.platform.application.in.product.ProductUseCase;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 상품 관련 기능을 담당하는 API 입니다.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController implements ProductControllerSpec {

    private final ProductUseCase productService;

    /**
     * 상품 목록 조회 & 필터링
     * @param pageRequest       페이지 요청
     * @param category          카테고리
     * @param manufacturer      제조사
     * @param minPrice          최소 가격
     * @param maxPrice          최대 가격
     * @param principalDetails  북마크 여부를 확인하기 위한 유저 체크
     */
    @GetMapping("/list")
    public ApiResponse<SliceResponse<ProductListResponse>> getProductList(PageRequest pageRequest,
                                                                          @RequestParam CategoryType category,
                                                                          @RequestParam(required = false) List<String> manufacturer,
                                                                          @RequestParam(required = false) Integer minPrice,
                                                                          @RequestParam(required = false) Integer maxPrice,
                                                                          @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        /// Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        /// 공통 객체들 저장
        Slice<ProductListResponse> products;

        /// 유저가 없다면 null 저장
        UUID userId = principalDetails != null ? principalDetails.getId() : null;

        // 파라미터 여부에 따라 분기 처리
        if (manufacturer == null && minPrice == null && maxPrice == null) {
            /// 카테고리만 있는 경우
            products = productService.getProductsByCategoryId(userId, category.getValue(), pageable);

        } else if (manufacturer != null && minPrice == null && maxPrice == null) {
            /// 카테고리 + 제조사만 있는 경우
            products = productService.getProductsByCategoryIdAndManufacturerId(userId, category.getValue(), manufacturer, pageable);

        } else if (manufacturer == null && minPrice != null && maxPrice != null) {
            /// 카테고리 + 가격만 있는 경우
            products = productService.getProductsByCategoryIdAndPrice(userId, category.getValue(), minPrice, maxPrice, pageable);

        } else if (manufacturer != null && minPrice != null && maxPrice != null) {
            /// 카테고리 + 제조사 + 가격이 있는 경우
            products = productService.getProductsByCategoryIdAndManufacturerIdAndPrice(
                    userId, category.getValue(), manufacturer, minPrice, maxPrice, pageable);
        }  else {
            throw new IllegalArgumentException(ErrorCode.BAD_REQUEST.getMessage());
        }

        return ApiResponse.ok(SliceResponse.from(products));
    }

    /**
     * 상품 상세 조회 API
     * @param id    상세 조회할 ID
     */
    @GetMapping
    public ApiResponse<ProductDetailResponse> getProduct(
            @RequestParam String id,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        /// 유저가 없다면 null 저장
        UUID userId = principalDetails != null ? principalDetails.getId() : null;

        // 서비스 호출
        ProductDetailResponse product = productService.getProduct(userId, id);

        return ApiResponse.ok(product);
    }
}

