package site.kikihi.custom.platform.application.in.bookmark;

import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark.BookmarkSyncRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.bookmark.BookmarkListResponse;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.*;

/**
 * [유저의 관심 상품을 담아주는 북마크 인터페이스] 입니다.
 * - 북마크 저장
 * - 북마크 조회
 * - 북마크 수정
 * - 북마크 삭제의 기능을 수행합니다.
 */


public interface BookmarkUseCase {

    /// 저장하기
    Bookmark saveBookmark(UUID userId, BookmarkRequest request);

    Slice<BookmarkListResponse> loadBookmarksByUserIdAndCategory(UUID userId, String category, Pageable pageable);

    /// 비회원을 위한 싱크 맞추기
    void syncBookmarks(UUID userId, BookmarkSyncRequest request);

    /// 삭제하기
    void deleteBookmarkById(Long id, UUID userId);

    void deleteBookmarkById(List<Long> ids, UUID userId);

}
