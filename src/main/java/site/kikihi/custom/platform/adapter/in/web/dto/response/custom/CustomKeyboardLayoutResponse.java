package site.kikihi.custom.platform.adapter.in.web.dto.response.custom;

import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.util.List;

/**
 *
 * @param typeName
 * @param description
 */

@Builder
@Schema(
        name = "[응답][커스텀] 커스텀 키보드 레이아웃 목록 조회 Response",
        description = "커스텀 키보드의 레이아웃 타입 목록을 반환하는 응답 DTO입니다."
)
public record CustomKeyboardLayoutResponse(
        @Schema(description = "커스텀 키보드 레이아웃 타입명", example = "65%")
        String typeName,

        @Schema(description = "레이아웃 설명", example = "알맞은 키 수와 컴팩트한 사이즈의 배치")
        String description
) {

    /// 정적 팩토리 메서드
    public static CustomKeyboardLayoutResponse from(CustomKeyboardLayout layout) {
        return CustomKeyboardLayoutResponse.builder()
                .typeName(layout.getLayoutName())
                .description(layout.getDescription())
                .build();
    }

    /// 정적 팩토리 메서드
    public static List<CustomKeyboardLayoutResponse> from(List<CustomKeyboardLayout> layouts) {
        return layouts.stream()
                .map(CustomKeyboardLayoutResponse::from)
                .toList();
    }
}
