package com.jiyoung.kikihi.platform.adapter.in.web.swagger;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.page.SliceResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.CustomKeyBoardRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.custom.CustomKeyboardDetailResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.custom.CustomKeyboardListResponse;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "커스텀 키보드 API", description = "커스텀 키보드 관련 API입니다.")
public interface CustomKeyboardControllerSpec {


    /**
     * 키보드 생성
     * @param request           생성 DTO
     * @param principalDetails  유저
     */
    @Operation(
            summary = "커스텀 키보드 생성 API",
            description = "JWT기반으로 커스텀 키보드를 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "북마크 생성 성공 예시", value = REQUEST),
                            }
                    )
            )
    )
    ApiResponse<String> createCustomKeyboard(
            @RequestBody @Valid CustomKeyBoardRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails);


    /**
     * 키보드 상세 조회
     * @param customKeyboardId  키보드 ID
     */
    @Operation(
            summary = "커스텀 키보드 상세 조회 API",
            description = "커스텀 키보드를 상세 조회합니다."
    )
    ApiResponse<CustomKeyboardDetailResponse> getCustomKeyBoard(
            @PathVariable Long customKeyboardId);


    /**
     * 키보드 목록 조회
     * @param principalDetails  유저
     */
    @Operation(
            summary = "커스텀 키보드 목록 조회 API",
            description = "JWT기반으로 나의 커스텀 키보드 목록을 조회합니다."
    )
    ApiResponse<SliceResponse<CustomKeyboardListResponse>> getMyCustoms(
            @AuthenticationPrincipal PrincipalDetails principalDetails);


    String REQUEST = """
            {
              "layout" : "PERCENT_75",
              "housingId" : "686bd26d34c3c12ea9b8e7e2",
              "switchId" : "686bd26d34c3c12ea9b8e7e3",
              "keyCapId" : "686bd26d89b5df14c6125f58",
              "name" : "테스트 커스텀"
            }
            """;

}
