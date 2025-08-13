package site.kikihi.custom.platform.adapter.in.web.dto.request.custom;

import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 커스텀 키보드를 만들기 위한 요청 DTO
 */
@Data
@Schema(
        name = "[요청][커스텀] 커스텀 키보드 생성 Request",
        description = "사용자가 직접 커스텀 키보드를 생성할 때 사용하는 요청 DTO입니다."
)
public class CustomKeyboardRequest {

    @Schema(description = "커스텀 키보드 레이아웃 정보", implementation = CustomKeyboardLayout.class)
    private CustomKeyboardLayout layout;

    @Schema(description = "하우징(프레임) ID", example = "frame_04")
    private String housingId;

    @Schema(description = "스위치 ID", example = "switch_11")
    private String switchId;

    @Schema(description = "키캡 ID", example = "keycap_29")
    private String keyCapId;

    @Schema(description = "커스텀 키보드 이름", example = "내 인생 첫 커스텀 65키")
    private String name;

}
