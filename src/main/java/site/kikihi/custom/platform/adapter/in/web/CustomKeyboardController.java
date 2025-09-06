package site.kikihi.custom.platform.adapter.in.web;

import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.global.response.page.SliceResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.request.custom.CustomCategoryType;
import site.kikihi.custom.platform.adapter.in.web.dto.request.custom.CustomKeyboardRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.custom.CustomKeyboardLayoutResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.custom.CustomKeyboardDetailResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.custom.CustomKeyboardListResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.adapter.in.web.swagger.CustomKeyboardControllerSpec;
import site.kikihi.custom.platform.application.in.custom.CustomKeyboardUseCase;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardWithName;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/custom")
@RequiredArgsConstructor
public class CustomKeyboardController implements CustomKeyboardControllerSpec {

    private final CustomKeyboardUseCase service;

    /**
     * 커스텀 키보드 저장
     *
     * @param request          요청 DTO
     * @param principalDetails 유저
     */
    @PostMapping()
    public ApiResponse<Void> createCustomKeyboard(
            @RequestBody @Valid CustomKeyboardRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        /// 서비스
        service.saveCustomKeyboard(request, principalDetails.getId());

        return ApiResponse.created();
    }

    /**
     * 커스텀 키보드 상세 조회
     *
     * @param customKeyboardId 키보드 상세 조회 ID
     */
    @GetMapping("/{customKeyboardId}")
    public ApiResponse<CustomKeyboardDetailResponse> getCustomKeyboard(
            @PathVariable Long customKeyboardId
    ) {
        /// 서비스
        CustomKeyboardWithName keyBoard = service.getCustomKeyboard(customKeyboardId);

        /// DTO 변경
        var response = CustomKeyboardDetailResponse.from(keyBoard);

        /// 결과 응답
        return ApiResponse.ok(response);
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
        Slice<CustomKeyboardWithName> boards = service.getCustomKeyboards(principalDetails.getId());

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
    public ApiResponse<List<CustomKeyboardLayoutResponse>> getCustomKeyboardLayout() {

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
    public ApiResponse<SliceResponse<ProductListResponse>> getCustomKeyboardProductsByLayout(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam CustomCategoryType category,
            @RequestParam CustomKeyboardLayout layout,
            PageRequest pageRequest
    ) {

        /// 유저가 없다면 null 저장
        UUID userId = principalDetails != null ? principalDetails.getId() : null;

        /// Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        /// 서비스
        Slice<ProductListResponse> content = service.getCustomProducts(userId, category.getValue(), layout, pageable);

        /// 개수 조회
        Long counts = service.getCustomProductCounts(category.getValue(), layout);

        /// DTO 변경
        Slice<ProductListResponse> dtoSlice = new SliceImpl<>(
                content.getContent(),
                content.getPageable(),
                content.hasNext()
        );

        /// 응답
        return ApiResponse.ok(SliceResponse.from(dtoSlice, counts));
    }

    /**
     * 커스텀 키보드 삭제 API
     * @param id                삭제할 커스텀 키보드
     * @param principalDetails  유저
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCustomKeyboard(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        /// 서비스
        service.deleteCustomKeyboard(id, principalDetails.getId());

        /// 응답
        return ApiResponse.deleted();

    }
}
