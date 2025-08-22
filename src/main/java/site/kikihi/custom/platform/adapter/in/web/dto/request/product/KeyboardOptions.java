package site.kikihi.custom.platform.adapter.in.web.dto.request.product;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(
        name = "[요청][상품] 키보드 옵션 Enum",
        description = "키보드 추천 서비스에서 사용하는 키보드 옵션 Enum입니다."
)
public class KeyboardOptions {

    @Getter
    @RequiredArgsConstructor
    @Schema(name = "[요청][상품] 배열(Size) Enum", description = "키보드 배열 옵션")
    public enum Size {
        @Schema(description = "텐키리스 배열", example = "tenkeyless")
        TENKEYLESS("tenkeyless"),

        @Schema(description = "풀배열", example = "full")
        FULL("full"),

        @Schema(description = "미니 배열", example = "mini")
        MINI("mini");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    @Getter
    @RequiredArgsConstructor
    @Schema(name = "[요청][상품] 키압(Key Pressure) Enum", description = "키압 옵션")
    public enum KeyPressure {
        @Schema(description = "가벼운 키압 (50g 미만)", example = "light")
        LIGHT("light"),

        @Schema(description = "보통 키압 (50g 이상)", example = "normal")
        NORMAL("normal");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    @Getter
    @RequiredArgsConstructor
    @Schema(name = "[요청][상품] 키압(Key Pressure) Enum", description = "키압 옵션")
    public enum Layout {
        @Schema(description = "인체공학적 (스텝스컬쳐2)", example = "egonomic")
        ERGONOMIC("egonomic"),

        @Schema(description = "심플하고 깔끔한 (low 프로파일)", example = "simple")
        SIMPLE("simple");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    @Getter
    @RequiredArgsConstructor
    @Schema(name = "[요청][상품] 스위치 종류(Switch Type) Enum", description = "스위치 종류 옵션")
    public enum SwitchType {

        @Schema(description = "조용한", example = "silent")
        SILENT("silent"),

        @Schema(description = "적당한", example = "normal")
        NORMAL("normal"),

        @Schema(description = "강한", example = "loud")
        LOUD("loud"),

        @Schema(description = "부드러운", example = "smooth")
        SMOOTH("smooth");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    @Getter
    @RequiredArgsConstructor
    @Schema(name = "[요청][상품] 흡음재 여부 Enum", description = "흡음재 유무 옵션")
    public enum SoundDampener {
        @Schema(description = "흡음재 있음", example = "○")
        YES("○"),

        @Schema(description = "흡음재 없음", example = "X")
        NO("X");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    @Getter
    @RequiredArgsConstructor
    @Schema(name = "[요청][상품] RGB 여부 Enum", description = "RGB 유무 옵션")
    public enum RGB {
        @Schema(description = "RGB 있음", example = "○")
        YES("○"),

        @Schema(description = "RGB 없음", example = "X")
        NO("X");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }


}
