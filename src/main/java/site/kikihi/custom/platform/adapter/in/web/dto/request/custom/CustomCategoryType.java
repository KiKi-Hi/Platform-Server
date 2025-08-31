package site.kikihi.custom.platform.adapter.in.web.dto.request.custom;


import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(
        name = "[요청][커스텀] 커스텀 카테고리 타입 Enum",
        description = "커스텀에서 사용하는 카테고리 구분 타입 Enum입니다."
)
public enum CustomCategoryType {

    @Schema(description = "하우징(프레임)", example = "housing")
    HOUSING("housing"),

    @Schema(description = "스위치", example = "switch")
    SWITCH("switch"),

    @Schema(description = "키캡", example = "keycap")
    KEYCAP("keycap"),

    @Schema(description = "액세서리(기타)", example = "accessory")
    ACCESSORY("accessory"),
    ;

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
