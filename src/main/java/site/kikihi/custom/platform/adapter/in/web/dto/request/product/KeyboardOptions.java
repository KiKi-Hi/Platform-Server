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
        TENKEYLESS("텐키리스"),

        @Schema(description = "풀배열", example = "full")
        FULL("풀배열"),

        @Schema(description = "미니 배열", example = "mini")
        MINI("미니");

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
    @Schema(name = "[요청][상품] 키캡 배열(Keycap Profile) Enum", description = "키캡 배열 옵션")
    public enum KeycapProfile {
        @Schema(description = "OEM 키캡 배열", example = "oem")
        OEM("oem"),

        @Schema(description = "Cherry 키캡 배열", example = "cherry")
        CHERRY("cherry"),

        @Schema(description = "SA 키캡 배열", example = "sa")
        SA("sa"),

        @Schema(description = "DSA 키캡 배열", example = "dsa")
        DSA("dsa"),

        @Schema(description = "XDA 키캡 배열", example = "xda")
        XDA("xda");

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
        @Schema(description = "적축 스위치", example = "적축")
        RED("적축"),

        @Schema(description = "갈축 스위치", example = "갈축")
        BROWN("갈축"),

        @Schema(description = "청축 스위치", example = "청축")
        BLUE("청축"),

        @Schema(description = "백축 스위치", example = "백축")
        WHITE("백축"),

        @Schema(description = "자석축 스위치", example = "자석축")
        MAGNETIC("자석축");

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

//    @Getter
//    @RequiredArgsConstructor
//    @Schema(name = "[요청][상품] 제조사(Brand) Enum", description = "제조회사 옵션")
//    public enum Brand {
//        @Schema(description = "앱코 제조사", example = "abko")
//        ABKO("abko"),
//
//        @Schema(description = "COX 제조사", example = "cox")
//        COX("cox"),
//
//        @Schema(description = "CHERRY 제조사", example = "cherry_brand")
//        CHERRY("cherry_brand"),
//
//        @Schema(description = "한성컴퓨터 제조사", example = "hansung")
//        HANSUNG("hansung"),
//
//        @Schema(description = "Keychron 제조사", example = "keychron")
//        KEYCHRON("keychron"),
//
//        @Schema(description = "CORSAIR 제조사", example = "corsair")
//        CORSAIR("corsair"),
//
//        @Schema(description = "기타 제조사", example = "other")
//        OTHER("other");
//
//        private final String value;
//
//        @JsonValue
//        public String getValue() {
//            return value;
//        }
//    }

    @Getter
    @RequiredArgsConstructor
    @Schema(name = "[요청][상품] 키캡 재질(Keycap Material) Enum", description = "키캡 재질 옵션")
    public enum KeycapMaterial {
        @Schema(description = "PBT 키캡 재질", example = "pbt")
        PBT("pbt"),

        @Schema(description = "ABS 키캡 재질", example = "abs")
        ABS("abs"),

        @Schema(description = "POM 키캡 재질", example = "pom")
        POM("pom");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }

}
