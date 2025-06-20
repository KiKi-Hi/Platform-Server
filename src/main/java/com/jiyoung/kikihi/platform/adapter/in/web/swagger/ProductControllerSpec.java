package com.jiyoung.kikihi.platform.adapter.in.web.swagger;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "상품 관련 API", description = "상품 기능을 수행하는 API 입니다.")
public interface ProductControllerSpec {

    @Operation(
            summary = "테스트용 상품 조회 API",
            description = "상품 목록을 조회할 수 있습니다."
    )
    public ApiResponse<List<ProductListResponse>> getProducts();

}
