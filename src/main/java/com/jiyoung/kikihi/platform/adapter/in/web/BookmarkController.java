package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.page.PageRequest;
import com.jiyoung.kikihi.global.response.page.SliceResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.product.CategoryType;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.bookmark.BookmarkListResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.BookmarkControllerSpec;
import com.jiyoung.kikihi.platform.application.in.bookmark.BookmarkUseCase;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

        // 아이디 추출
        UUID userId = principalDetails.getId();
        request.setUserId(userId);

        // 서비스 계층
        service.saveBookmark(request);

        // 리턴
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
     * @param bookmarkId        북마크 ID
     * @param principalDetails  유저 ID
     */
    @DeleteMapping("/{bookmarkId}")
    public ApiResponse<String> deleteBookmark(@PathVariable Long bookmarkId,@AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 아이디 추출
        UUID userId = principalDetails.getId();

        // 서비스 계층
        service.deleteBookmarkById(bookmarkId, userId);

        return ApiResponse.ok("성공적으로 삭제 되었습니다.");
    }
}
