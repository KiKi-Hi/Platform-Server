package com.kikihi.store.platform.adapter.in.web;

import com.kikihi.store.global.response.ApiResponse;
import com.kikihi.store.global.response.page.PageRequest;
import com.kikihi.store.global.response.page.SliceResponse;
import com.kikihi.store.platform.adapter.in.web.dto.request.CustomKeyBoardRequest;
import com.kikihi.store.platform.adapter.in.web.dto.response.custom.CustomKeyboardLayoutResponse;
import com.kikihi.store.platform.adapter.in.web.dto.response.custom.CustomKeyboardDetailResponse;
import com.kikihi.store.platform.adapter.in.web.dto.response.custom.CustomKeyboardListResponse;
import com.kikihi.store.platform.adapter.in.web.dto.response.product.ProductListResponse;
import com.kikihi.store.platform.adapter.in.web.swagger.CustomKeyboardControllerSpec;
import com.kikihi.store.platform.application.in.custom.CustomKeyboardUseCase;
import com.kikihi.store.platform.application.in.product.ProductUseCase;
import com.kikihi.store.platform.domain.custom.CustomKeyboardLayout;
import com.kikihi.store.platform.domain.custom.CustomKeyboardWithName;
import com.kikihi.store.security.oauth2.domain.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/custom")
@RequiredArgsConstructor
public class CustomKeyboardController implements CustomKeyboardControllerSpec {

    private final CustomKeyboardUseCase service;

    /// 상품 조회를 위한 의존성
    private final ProductUseCase productService;

    /**
     * 커스텀 키보드 저장
     *
     * @param request          요청 DTO
     * @param principalDetails 유저
     */
    @PostMapping()
    public ApiResponse<String> createCustomKeyboard(
            @RequestBody @Valid CustomKeyBoardRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        /// 서비스
        service.saveCustomKeyBoard(request, principalDetails.getId());

        return ApiResponse.created("정상적으로 생성되었습니다.");
    }

    /**
     * 커스텀 키보드 상세 조회
     *
     * @param customKeyboardId 키보드 상세 조회 ID
     */
    @GetMapping("/{customKeyboardId}")
    public ApiResponse<CustomKeyboardDetailResponse> getCustomKeyBoard(
            @PathVariable Long customKeyboardId
    ) {
        /// 서비스
        CustomKeyboardWithName keyBoard = service.getCustomKeyBoard(customKeyboardId);

        /// DTO 변경
        var response = CustomKeyboardDetailResponse.from(keyBoard);

        /// 결과 응답
        return ApiResponse.created(response);
    }

    /**
     * 커스텀 키보드 목록 조회
     *
     * @param principalDetails 유저
     */
    @GetMapping("/myCustoms")
    public ApiResponse<SliceResponse<CustomKeyboardListResponse>> getMyCustoms(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        /// 서비스
        Slice<CustomKeyboardWithName> boards = service.getCustomKeyBoards(principalDetails.getId());

        /// DTO로 변환
        List<CustomKeyboardListResponse> content = boards.getContent().stream()
                .map(CustomKeyboardListResponse::from)
                .toList();

        Slice<CustomKeyboardListResponse> dtoSlice = new SliceImpl<>(
                content,
                boards.getPageable(),
                boards.hasNext()
        );

        /// 결과 응답
        return ApiResponse.ok(SliceResponse.from(dtoSlice));
    }

    /**
     * 키보드 배열 종류 조회
     */
    @GetMapping("/layout")
    public ApiResponse<List<CustomKeyboardLayoutResponse>> getCustomKeyBoardLayout() {

        /// 서비스
        List<CustomKeyboardLayout> layouts = service.getKeyboardLayouts();

        /// DTO
        List<CustomKeyboardLayoutResponse> responses = CustomKeyboardLayoutResponse.from(layouts);

        return ApiResponse.ok(responses);
    }

    /**
     * 배열에 맞는 상품 조회
     */
    @GetMapping("/products")
    public ApiResponse<SliceResponse<ProductListResponse>> getCustomKeyBoardProductsByLayout(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam String categoryId,
            @RequestParam CustomKeyboardLayout layout,
            PageRequest pageRequest
    ) {

        /// Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        /// 서비스
        Slice<ProductListResponse> content = productService.getProductsByLayout(principalDetails.getId(), categoryId, layout, pageable);

        /// DTO 변경
        Slice<ProductListResponse> dtoSlice = new SliceImpl<>(
                content.getContent(),
                content.getPageable(),
                content.hasNext()
        );

        /// 응답
        return ApiResponse.ok(SliceResponse.from(dtoSlice));
    }

    /**
     * 커스텀 키보드 삭제 API
     * @param id                삭제할 커스텀 키보드
     * @param principalDetails  유저
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCustomKeyBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        /// 서비스
        service.deleteCustomKeyBoard(id, principalDetails.getId());

        /// 응답
        return ApiResponse.deleted("정상적으로 삭제되었습니다.");

    }
}
