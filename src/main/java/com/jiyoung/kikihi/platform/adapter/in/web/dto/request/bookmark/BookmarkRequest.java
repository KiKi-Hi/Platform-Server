package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.bookmark;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BookmarkRequest {

    private String productId;

    private UUID userId;

}
