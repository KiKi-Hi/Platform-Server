package com.jiyoung.kikihi.platform.adapter.in.web.dto.response.bookmark;

import com.jiyoung.kikihi.platform.domain.bookmark.Bookmark;
import lombok.Builder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Bookmark 대한 결과 DTO
 * @param id
 * @param userId
 * @param productId
 */

@Builder
public record BookmarkResponse(
        Long id,
        UUID userId,
        String productId
) {

    public static BookmarkResponse from(Bookmark bookmark) {
        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .userId(bookmark.getUserId())
                .productId(bookmark.getProductId())
                .build();
    }

    public static List<BookmarkResponse> from(List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(BookmarkResponse::from)
                .collect(Collectors.toList());
    }
}
