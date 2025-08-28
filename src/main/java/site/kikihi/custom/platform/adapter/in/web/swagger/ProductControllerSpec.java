package site.kikihi.custom.platform.adapter.in.web.swagger;

import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.global.response.page.SliceResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.CategoryType;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductDetailResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "상품 조회 API", description = "상품 조회를 수행하는 API 입니다.")
public interface ProductControllerSpec {

    /**
     * 상품 상세 조회 API
     * @param id                상품 아이디
     * @param principalDetails  유저
     */
    @Operation(
            summary = "상품 상세 조회 API",
            description = "ID를 바탕으로 상품 상세정보를 조회할 수 있습니다."
    )
    ApiResponse<ProductDetailResponse> getProduct(
            @Parameter(description = "상품 ID", example = "6896ed7d5198cf586e933d6e")
            @RequestParam String id,

            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principalDetails);

    /**
     * 상품 목록 조회 API
     * @param pageRequest           페이지
     * @param category              카테고리
     * @param manufacturer          제조사
     * @param minPrice              최소 금액
     * @param maxPrice              최대 금액
     * @param principalDetails      유저
     */
    @Operation(
            summary = "상품 목록 조회 API",
            description = "파라미터에 따라서 상품 목록을 조회할 수 있습니다."
    )
    ApiResponse<SliceResponse<ProductListResponse>> getProductList(
            PageRequest pageRequest,

            @Parameter(description = "카테고리", required = true, example = "keyboard")
            @RequestParam CategoryType category,

            @Parameter(
                    description = "제조사 목록 (여러 개 입력 가능)"
            )
            @RequestParam(required = false) List<String> manufacturer,

            @Parameter(description = "최소 가격")
            @RequestParam(required = false) Integer minPrice,

            @Parameter(description = "최대 가격")
            @RequestParam(required = false) Integer maxPrice,

            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principalDetails);


    @Operation(
            summary = "제조사 목록 조회 API",
            description = "카테고리에 따라서 제조사 목록을 조회할 수 있습니다."
    )
    ApiResponse<SliceResponse<String>> getManufacturers(
            PageRequest pageRequest,

            @Parameter(description = "카테고리", required = true, example = "keyboard")
            @RequestParam CategoryType category);
}

