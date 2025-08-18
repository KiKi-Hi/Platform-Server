package site.kikihi.custom.platform.adapter.in.web.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "[요청][상품] 키보드 추천 요청 DTO", description = "키보드 추천 요청에 사용하는 DTO입니다.")
public class KeyboardRecommendationRequest {

    @Schema(description = "키보드 배열(Size) 옵션", example = "텐키리스", required = false)
    private KeyboardOptions.Size size;

    @Schema(description = "키압(Key Pressure) 옵션", example = "light", required = false)
    private KeyboardOptions.KeyPressure keyPressure;

    @Schema(description = "키캡 배열(Keycap Profile) 옵션", example = "oem", required = false)
    private KeyboardOptions.KeycapProfile keycapProfile;

    @Schema(description = "스위치 종류(Switch Type) 옵션", example = "적축", required = false)
    private KeyboardOptions.SwitchType switchType;

    @Schema(description = "흡음재(Sound Dampener) 적용 여부", example = "○", required = false)
    private KeyboardOptions.SoundDampener soundDampener;

    @Schema(description = "RGB 적용 여부", example = "○", required = false)
    private KeyboardOptions.RGB rgb;

    @Schema(description = "제조사(Brand) 옵션", example = "앱코", required = false)
    private String brand;

    @Schema(description = "키캡 재질(Keycap Material) 옵션", example = "pbt", required = false)
    private KeyboardOptions.KeycapMaterial keycapMaterial;

    @Schema(description = "최소 가격 (단위: 원)", example = "0", required = false)
    private int minPrice;

    @Schema(description = "최대 가격 (단위: 원)", example = "200000", required = false)
    private int maxPrice;
}
