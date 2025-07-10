package com.jiyoung.kikihi.platform.adapter.in.web;


import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.order.AddToCartRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.CartControllerSpec;
import com.jiyoung.kikihi.platform.application.in.order.CartUseCase;
import com.jiyoung.kikihi.platform.domain.product.Product;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController implements CartControllerSpec {

    private final CartUseCase cartUseCase;


    /**
     * 장바구니에 상품을 담습니다.
     *
     * @param request 장바구니 저장요청 DTO
     * @return 생성 성공 메시지
     */
    @PostMapping
    public ApiResponse<String> addProductToCart(@RequestBody @Valid AddToCartRequest request, @AuthenticationPrincipal PrincipalDetails user) {
        cartUseCase.addProductToCart(request.productId(), request.quantity(), user.getId());
        return ApiResponse.created("성공적으로 상품을 장바구니에 저장했습니다.");
    }

    /**
     * 장바구니에 담긴 상품 목록을 조회합니다.
     *
     * @param principalDetails 인증된 사용자 정보
     * @return 상품 목록
     */
    @GetMapping
    public ApiResponse<List<ProductListResponse>> getCartProducts(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<Product> products = cartUseCase.getCartProducts(principalDetails.getId());
        List<ProductListResponse> productResponses = products.stream()
                .map(ProductListResponse::from)
                .toList();
        return ApiResponse.ok(productResponses);
    }

    /**
     * 장바구니에 담긴 상품을 삭제합니다.
     *
     * @param productId 상품 ID (PathVariable)
     * @param principalDetails 인증된 사용자 정보
     * @return 삭제 성공 메세지
     */   
    @DeleteMapping("/{productId}")
    public ApiResponse<String> removeProductFromCart(@PathVariable String productId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        cartUseCase.removeProductFromCart(productId, principalDetails.getId());
        return ApiResponse.ok("성공적으로 장바구니에서 상품을 삭제했습니다.");
    }


    /**
     * 장바구니에 담긴 상품 수량을 변경합니다.
     * @param productId 상품 ID (PathVariable)
     * @param quantity 변경할 수량 (RequestParam)
     * @param principalDetails 인증된 사용자 정보
     * @return 수량 변경 성공 메세지
     */
    @PutMapping("/{productId}")
    public ApiResponse<String> updateProductQuantityInCart(
            @PathVariable @NotNull String productId,
            @RequestParam @Min(value = 1, message = "수량은 1 이상이어야 합니다.") Integer quantity,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        cartUseCase.updateProductQuantityInCart(productId, quantity, principalDetails.getId());
        return ApiResponse.ok("성공적으로 장바구니 상품의 수량을 변경했습니다.");
    }


}
