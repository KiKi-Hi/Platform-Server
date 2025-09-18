package site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
@Schema(
        name = "[요청][북마크] 북마크 싱크 Request",
        description = "상품을 하번에 북마크할 때 사용하는 요청 DTO입니다."
)
public class BookmarkSyncRequest {

    @Schema(description = "북마크할 상품들", example = "6896ed7d5198cf586e933d6e")
    private List<@Valid BookmarkRequest> productIds;


}
