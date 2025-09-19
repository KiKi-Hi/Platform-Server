package site.kikihi.custom.platform.adapter.in.web.dto.request.bookmark;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(
        name = "[요청][북마크] 북마크 등록 Request",
        description = "사용자가 상품을 북마크할 때 사용하는 요청 DTO입니다."
)
public class BookmarkRequest {

    @Schema(description = "북마크할 상품 ID", example = "6896ed7d5198cf586e933d6e")
    @NotNull
    private String productId;

}
