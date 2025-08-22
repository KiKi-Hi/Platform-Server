package site.kikihi.custom.platform.adapter.in.web.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "[요청][상품] 키보드 추천 요청 DTO", description = "키보드 추천 요청에 사용하는 DTO입니다.")
public class KeyboardRecommendationRequest {

    @Schema(description = "키보드 배열(Size) 옵션", example = "ten", required = true)
    private KeyboardOptions.Size size;

    @Schema(description = "키압(Key Pressure) 옵션", example = "light", required = true)
    private KeyboardOptions.KeyPressure keyPressure;

    @Schema(description = "레이아웃 종류(Layout) 옵션", example = "egonomic", required = true)
    private KeyboardOptions.Layout layout;

    @Schema(description = "스위치 종류(Switch Type) 옵션", example = "silent", required = true)
    private KeyboardOptions.SwitchType switchType;

    @Schema(description = "흡음재(Sound Dampener) 적용 여부", example = "○", required = true)
    private KeyboardOptions.SoundDampener soundDampener;

    @Schema(description = "RGB 적용 여부", example = "○", required = true)
    private KeyboardOptions.RGB rgb;

    @Schema(description = "최소 가격 (단위: 원)", example = "0", required = true)
    private int minPrice;

    @Schema(description = "최대 가격 (단위: 원)", example = "200000", required = true)
    private int maxPrice;
}
