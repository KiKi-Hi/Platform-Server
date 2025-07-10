package com.jiyoung.kikihi.platform.adapter.in.web.swagger;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.AddToCartRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "장바구니 저장 & 조회 API", description = "장바구니에 상품을 담고 조회하는 API 입니다.")
public interface CartControllerSpec {

    @Operation(
            summary = "장바구니에 상품 추가",
            description = "장바구니에 상품을 담습니다."
    )
    @PostMapping
    ApiResponse<String> addProductToCart(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "장바구니 저장 요청 DTO",
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    implementation = AddToCartRequest.class,
                                    example = "{\"productId\": \"68628bc0b3ef08a40a70f2d8\", \"quantity\": 2}"
                            )
                    )
            )            @RequestBody @Valid AddToCartRequest request,
            @AuthenticationPrincipal PrincipalDetails user);

    @Operation(
            summary = "장바구니 상품 목록 조회",
            description = "장바구니에 담긴 상품 목록을 조회합니다."
    )
    @GetMapping
    ApiResponse<List<ProductListResponse>> getCartProducts(@AuthenticationPrincipal PrincipalDetails principalDetails);

    @Operation(
            summary = "장바구니 상품 삭제",
            description = "장바구니에 담긴 상품을 삭제합니다."
    )
    @DeleteMapping("/{productId}")
    ApiResponse<String> removeProductFromCart(
            @Parameter(description = "productId", example = "68628bc0b3ef08a40a70f2d8")
            @PathVariable String productId,
            @AuthenticationPrincipal PrincipalDetails principalDetails);


    @Operation(
            summary = "장바구니 상품 수량 변경",
            description = "장바구니에 담긴 상품의 수량을 변경합니다."
    )
    @PutMapping("{productId}")
    ApiResponse<String> updateProductQuantityInCart(
            @Parameter(description = "productId", example = "68628bc0b3ef08a40a70f2d8")
            @PathVariable @NotNull String productId,

            @Parameter(description = "수량", example = "2")
            @RequestParam @Min(value = 1, message = "수량은 1 이상이어야 합니다.") Integer quantity,
            @AuthenticationPrincipal PrincipalDetails principalDetails);

}
