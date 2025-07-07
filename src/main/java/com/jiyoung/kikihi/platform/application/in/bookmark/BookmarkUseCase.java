package com.jiyoung.kikihi.platform.application.in.bookmark;

import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.BookmarkRequest;
import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;

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
    Bookmark saveBookmark(BookmarkRequest request);

    /// 조회하기
    Bookmark loadBookmarkById(Long id);

    List<Bookmark> loadBookmarksByUserIdAndCategory(UUID userId, String category);

    /// 삭제하기
    void deleteBookmarkById(Long id, UUID userId);

}
