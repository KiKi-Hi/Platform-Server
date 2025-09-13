package site.kikihi.custom.platform.adapter.in.web.swagger;

import io.swagger.v3.oas.annotations.Parameter;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.global.response.page.SliceResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.request.custom.CustomCategoryType;
import site.kikihi.custom.platform.adapter.in.web.dto.request.custom.CustomKeyboardRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.custom.CustomKeyboardLayoutResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.custom.CustomKeyboardDetailResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.custom.CustomKeyboardListResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    ApiResponse<Void> createCustomKeyboard(
            @RequestBody @Valid CustomKeyboardRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails);


    /**
     * 키보드 상세 조회
     *
     * @param customKeyboardId 키보드 ID
     */
    @Operation(
            summary = "커스텀 키보드 상세 조회 API",
            description = "커스텀 키보드를 상세 조회합니다."
    )
    ApiResponse<CustomKeyboardDetailResponse> getCustomKeyboard(
            @Parameter(example = "1")
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

    /**
     *
     */

    @Operation(
            summary = "키보드 배열에 따른 가능한 부품 조회 API",
            description = "키보드 배열에 따라서 가능한 상품 목록을 조회합니다. 유저의 정보가 들어온다면 북마크 여부 또한 제공합니다."
    )
    ApiResponse<SliceResponse<ProductListResponse>> getCustomKeyboardProductsByLayout(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam CustomCategoryType category,
            @RequestParam CustomKeyboardLayout layout,
            PageRequest pageRequest
    );


    @Operation(
            summary = "커스텀 키보드 삭제 API",
            description = "JWT를 기반으로 커스텀 키보드를 삭제합니다."
    )
    ApiResponse<Void> deleteCustomKeyboard(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    String REQUEST = """
            {
              "layout" : "PERCENT_60",
              "housingId" : "68b3f4fedc26d32d8881fe12",
              "switchId" : "689d4fd15f80b10f1691fc96",
              "keyCapId" : "68b3f653dc26d32d8881fe43",
              "accessoryId" : "68baf9e37de370ef29647eb1",
              "name" : "테스트 커스텀"
            }
            """;

}
