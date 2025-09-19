package site.kikihi.custom.platform.adapter.in.web.dto.request.custom;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "[요청][커스텀] 커스텀 키보드 수정 Request",
        description = "사용자가 커스텀 키보드의 부품을 수정할 때 사용하는 요청 DTO입니다."
)
public class CustomKeyboardUpdateRequest {

    @Parameter(example = "1")
    private Long id;

    @Parameter(example = "housing")
    private CustomCategoryType category;

    @Parameter(example = "68b3f4fedc26d32d8881fe0e")
    private String productId;

}
