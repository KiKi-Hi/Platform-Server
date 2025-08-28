package site.kikihi.custom.platform.adapter.in.web.swagger;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.global.response.page.SliceResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkSyncRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.CategoryType;
import site.kikihi.custom.platform.adapter.in.web.dto.response.bookmark.BookmarkListResponse;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "북마크 API", description = "북마크 기능을 수행하는 API 입니다.")
public interface BookmarkControllerSpec {

    /**
     * 북마크를 생성합니다.
     *
     * @param request 북마크 생성 요청 DTO
     * @param principalDetails 인증 정보
     * @return 생성 성공 메시지
     */
    @Operation(
            summary = "북마크 생성 API",
            description = "JWT 기반으로 북마크를 생성할 수 있습니다."
    )
    ApiResponse<String> saveBookmark(
            @RequestBody @Valid BookmarkRequest request,

            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principalDetails);


    /**
     * 북마크 싱크합니다.
     *
     * @param request          북마크 생성 요청 DTO
     * @param principalDetails 인증 정보
     */
    @Operation(
            summary = "북마크 싱크 API",
            description = "JWT 기반으로 한번에 로컬 스토리지에 저장한 내용을 여러 개의 북마크에 대해서 싱크 맞춥니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "북마크 생성 성공 예시", value = SUCCESS_PAYLOAD),
                            }
                    )
            )
    )
    ApiResponse<String> synceBookmark(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody BookmarkSyncRequest request
    );



    /**
     * 나의 카테고리별 북마크 목록을 조회합니다.
     *
     * @param principalDetails 인증 정보
     * @param category         카테고리명
     * @return 북마크 목록 DTO 리스트
     */
    @Operation(
            summary = "나의 북마크 목록 조회 API",
            description = "JWT 기반으로 카테고리별 북마크 목록을 조회할 수 있습니다."
    )
    ApiResponse<SliceResponse<BookmarkListResponse>> loadBookmarkByCategory(

            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principalDetails,

            @Parameter(description = "카테고리", example = "keycap")
            @RequestParam CategoryType category,
            PageRequest pageRequest);


    @Operation(
            summary = "북마크를 삭제 API",
            description = "JWT 기반으로 본인이 기존에 북마크한 상품의 북마크를 해제할 수 있습니다."
    )
    @DeleteMapping("/{bookmarkId}")
    ApiResponse<String> deleteBookmark(

            @Parameter(example = "1")
            @PathVariable Long bookmarkId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal PrincipalDetails principalDetails);


    String SUCCESS_PAYLOAD = """
            {
               "productIds": [
                 { "productId": "689b3eb15198cf586e9341ba" },
                 { "productId": "689b3ea65198cf586e9341b9" },
                 { "productId": "689b3e995198cf586e9341b8" },
                 { "productId": "689b3e8b5198cf586e9341b7" },
                 { "productId": "689b3e785198cf586e9341b6" }
               ]
             }
            
            """;
}
