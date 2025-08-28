package site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class BookmarkSyncRequest {

    @Schema(description = "북마크할 상품들", example = "6896ed7d5198cf586e933d6e")
    private List<BookmarkRequest> productIds;

}
