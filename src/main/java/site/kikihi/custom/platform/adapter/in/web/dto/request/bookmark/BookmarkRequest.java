package site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(
        name = "[요청][북마크] 북마크 등록 Request",
        description = "사용자가 상품을 북마크할 때 사용하는 요청 DTO입니다."
)
public class BookmarkRequest {

    @Schema(description = "북마크할 상품 ID", example = "101")
    private String productId;

    @Schema(description = "사용자 UUID", example = "95ea60b2-f63b-434e-afc5-d5e5d6a505e7")
    private UUID userId;

}
