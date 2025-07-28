package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.page.SliceResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.CustomKeyBoardRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.custom.CustomKeyboardDetailResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.custom.CustomKeyboardListResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.CustomKeyboardControllerSpec;
import com.jiyoung.kikihi.platform.application.in.custom.CustomKeyboardUseCase;
import com.jiyoung.kikihi.platform.domain.custom.CustomKeyboardWithName;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ApiResponse<String> createCustomKeyboard(
            @RequestBody @Valid CustomKeyBoardRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        /// 서비스
        service.saveCustomKeyBoard(request, principalDetails.getId());

        return ApiResponse.created("정상적으로 생성되었습니다.");
    }

    /**
     * 커스텀 키보드 상세 조회
     * @param customKeyboardId  키보드 상세 조회 ID
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
     * @param principalDetails  유저
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



}
