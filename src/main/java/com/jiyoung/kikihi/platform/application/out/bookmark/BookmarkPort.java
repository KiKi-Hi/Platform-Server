package com.jiyoung.kikihi.platform.application.out.bookmark;

import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;

import java.util.*;

/**
 * [DB에 북마크를 저장하는 Port 인터페이스] 입니다.
 * - DB 저장
 * - DB 조회
 * - DB 삭제
 */

public interface BookmarkPort {

    /// 저장하기
    Bookmark saveBookmark(Bookmark bookmark);

    /// 조회하기
    Optional<Bookmark> getBookmark(Long id);

    List<Bookmark> getBookmarksByUserIdAndCategoryId(UUID userId, String category);

    boolean checkBookmarkByIdAndUserId(Long bookmarkId, UUID userId);

    boolean checkBookmarkByUserIdAndProductId(UUID userId, String productId);

    /// 삭제하기
    void deleteBookmarkById(Long bookmarkId);


    /// 서비스 외부 의존성 추가
    boolean checkBookmarkByProductIdAndUserId(String productId, UUID userId);

    /// 북마크 인기순
    Map<String, Long> getFavoriteBookmarks();

}
