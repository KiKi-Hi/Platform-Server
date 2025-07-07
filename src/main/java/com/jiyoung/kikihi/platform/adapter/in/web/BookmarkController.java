package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.BookmarkRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.response.bookmark.BookmarkResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.swagger.BookmarkControllerSpec;
import com.jiyoung.kikihi.platform.application.in.bookmark.BookmarkUseCase;
import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;
import com.jiyoung.kikihi.security.oauth2.domain.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ApiResponse.created("정상적으로 생성되었습니다.");
    }

    /**
     * 북마크 상세 정보를 조회합니다.
     *
     * @param id 북마크 ID (PathVariable)
     * @return 북마크 상세 응답 DTO
     */
    @GetMapping("/{id}")
    public ApiResponse<BookmarkResponse> loadBookmark(@PathVariable Long id) {

        // 서비스 계층
        Bookmark bookmark = service.loadBookmarkById(id);

        // 리턴
        return ApiResponse.ok(BookmarkResponse.from(bookmark));
    }

    /**
     * 카테고리별 북마크 목록을 조회합니다.
     *
     * @param principalDetails 인증된 사용자 정보
     * @param category 카테고리명 (Query Parameter)
     * @return 북마크 목록 응답 DTO 리스트
     */
    @GetMapping
    public ApiResponse<List<BookmarkResponse>> loadBookmarkByCategory(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam String category) {

        // 아이디 추출
        UUID userId = principalDetails.getId();

        // 서비스 계층
        List<Bookmark> bookmarks = service.loadBookmarksByUserIdAndCategory(userId, category);

        // 리턴
        return ApiResponse.ok(BookmarkResponse.from(bookmarks));
    }
}
