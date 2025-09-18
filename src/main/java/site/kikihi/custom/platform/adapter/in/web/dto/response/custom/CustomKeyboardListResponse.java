package site.kikihi.custom.platform.adapter.in.web.dto.response.custom;

import site.kikihi.custom.platform.domain.custom.CustomKeyboardWithName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.util.List;

/**
 * 커스텀 키보드 목록 조회 DTO
 * @param customId              커스텀 ID
 * @param customName            커스텀 이름
 * @param customKeyboardType    커스텀 타입
 * @param housingName           하우징 이름
 * @param keyCapName            키캡 이름
 * @param switchName            스위치 이름
 * @param thumbnail             썸네일
 * @param price                 가격
 */
@Builder
@Schema(
        name = "[응답][커스텀] 커스텀 키보드 목록 조회 Response",
        description = "사용자가 만든 커스텀 키보드 목록 조회 응답 DTO입니다."
)
public record CustomKeyboardListResponse(
        @Schema(description = "커스텀 키보드 ID", example = "101")
        Long customId,

        @Schema(description = "커스텀 키보드 이름", example = "나만의 무접점 65키")
        String customName,

        @Schema(description = "커스텀 키보드 타입", example = "65%")
        String customKeyboardType,

        @Schema(description = "하우징(프레임) 이름", example = "TX-65")
        String housingName,

        @Schema(description = "키캡 이름", example = "GMK Blue Samurai")
        String keyCapName,

        @Schema(description = "스위치 이름", example = "Gateron Ink Black v2")
        String switchName,

        @Schema(description = "악세사리 이름", example = "손목 보호대")
        String accessoryName,

        @Schema(description = "썸네일 이미지 URL", example = "https://example.com/custom/101.jpg")
        String thumbnail,

        @Schema(description = "총 가격(원)", example = "370000.0")
        double price
) {

    /// 정적 팩토리 메서드
    public static CustomKeyboardListResponse from(CustomKeyboardWithName entity){
        return CustomKeyboardListResponse.builder()
                .customId(entity.id())
                .customName(entity.name())
                .customKeyboardType(entity.layout())
                .housingName(entity.frameName())
                .keyCapName(entity.keyCapName())
                .switchName(entity.switchName())
                .accessoryName(entity.accessoryName())
                .thumbnail(entity.imageUrl())
                .price(entity.totalPrice())
                .build();
    }

    /// 정적 팩토리 메서드
    public static List<CustomKeyboardListResponse> from(List<CustomKeyboardWithName> entities){
        return entities.stream()
                .map(CustomKeyboardListResponse::from)
                .toList();
    }

}
