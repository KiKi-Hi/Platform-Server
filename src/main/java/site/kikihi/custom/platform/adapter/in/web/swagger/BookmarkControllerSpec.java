package site.kikihi.custom.platform.adapter.in.web.swagger;

import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.global.response.page.SliceResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.CategoryType;
import site.kikihi.custom.platform.adapter.in.web.dto.response.bookmark.BookmarkListResponse;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
}
