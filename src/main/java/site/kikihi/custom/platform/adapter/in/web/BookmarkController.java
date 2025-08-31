package site.kikihi.custom.platform.adapter.in.web;

import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.global.response.page.SliceResponse;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkSyncRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.CategoryType;
import site.kikihi.custom.platform.adapter.in.web.dto.response.bookmark.BookmarkListResponse;
import site.kikihi.custom.platform.adapter.in.web.swagger.BookmarkControllerSpec;
import site.kikihi.custom.platform.application.in.bookmark.BookmarkUseCase;
import site.kikihi.custom.security.oauth2.domain.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController implements BookmarkControllerSpec {

    private final BookmarkUseCase service;

    /**
     * 북마크를 생성합니다.
     *
     * @param request 북마크 생성 요청 DTO
     * @return 생성 성공 메시지
     */
    @PostMapping
    public ApiResponse<String> saveBookmark(
            @RequestBody @Valid BookmarkRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        /// 아이디 추출
        UUID userId = principalDetails.getId();

        /// 서비스 계층
        service.saveBookmark(userId, request);

        /// 리턴
        return ApiResponse.created();
    }

    /**
     * 로컬 스토리지에 저장한 북마크를 한번에 저장할 수 있도록 하는 서비스 로직
     *
     * @param request 북마크 생성 요청 DTO
     */
    @PostMapping("/sync")
    public ApiResponse<String> synceBookmark(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody BookmarkSyncRequest request
    ) {

        /// 아이디 추출
        UUID userId = principalDetails.getId();

        /// 서비스 계층
        service.syncBookmarks(userId, request);

        /// 리턴
        return ApiResponse.created();

    }

    /**
     * 나의 북마크 목록을 조회합니다.
     *
     * @param principalDetails 인증된 사용자 정보
     * @param category         카테고리명 (Query Parameter)
     * @return 북마크 목록 응답 DTO 리스트
     */
    @GetMapping
    public ApiResponse<SliceResponse<BookmarkListResponse>> loadBookmarkByCategory(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam CategoryType category,
            PageRequest pageRequest) {

        // Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1,
                pageRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        // 아이디 추출
        UUID userId = principalDetails.getId();

        // 서비스 계층
        Slice<BookmarkListResponse> responses = service.loadBookmarksByUserIdAndCategory(userId, category.getValue(), pageable);

        // SliceDTO 처리
        SliceResponse<BookmarkListResponse> sliceResponse = SliceResponse.from(responses);

        return ApiResponse.ok(sliceResponse);
    }

    /**
     * 북마크를 삭제하는 로직입니다.
     *
     * @param ids       북마크 ID
     * @param principalDetails 유저 ID
     */
    @DeleteMapping()
    public ApiResponse<String> deleteBookmark(
            @RequestParam List<Long> ids,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 아이디 추출
        UUID userId = principalDetails.getId();

        // 서비스 계층
        service.deleteBookmarkById(ids, userId);

        return ApiResponse.deleted();
    }
}
