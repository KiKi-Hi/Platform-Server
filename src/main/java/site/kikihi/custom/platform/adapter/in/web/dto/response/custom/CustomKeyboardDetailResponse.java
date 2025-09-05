package site.kikihi.custom.platform.adapter.in.web.dto.response.custom;

import site.kikihi.custom.platform.domain.custom.CustomKeyboardWithName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

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
        name = "[응답][커스텀] 커스텀 키보드 상세 조회 Response",
        description = "커스텀 키보드의 상세 정보를 조회하는 응답 DTO입니다."
)
public record CustomKeyboardDetailResponse(
        @Schema(description = "커스텀 키보드 ID", example = "1")
        Long customId,

        @Schema(description = "커스텀 키보드 이름", example = "타건감 좋은 65키 키보드")
        String customName,

        @Schema(description = "커스텀 키보드 타입", example = "60배열")
        String customKeyboardType,

        @Schema(description = "하우징 상품 ID", example = "68b3f4fedc26d32d8881fe12")
        String housingId,

        @Schema(description = "하우징(프레임) 이름", example = "TX-65")
        String housingName,

        @Schema(description = "키캡 ID", example = "GMK Red Samurai")
        String keyCapId,

        @Schema(description = "키캡 이름", example = "GMK Red Samurai")
        String keyCapName,

        @Schema(description = "스위치 ID", example = "689d4fd15f80b10f1691fc96")
        String switchId,

        @Schema(description = "스위치 이름", example = "게이트론 브라운 스위치 (10개) - 갈축 10개")
        String switchName,

        @Schema(description = "악세사리 ID", example = "68baf9e37de370ef29647eb1")
        String accessoryId,

        @Schema(description = "악세사리 이름", example = "아크릴 키보드 덮개 케이스 커버 보관함 방진 중형 보호 먼지 차단 투명 수납함 정리함")
        String accessoryName,

        @Schema(description = "썸네일 이미지 URL", example = "https://example.com/custom/101.jpg")
        String thumbnail,

        @Schema(description = "총 가격(원)", example = "375000.0")
        double price
) {

    /// 정적 팩토리 메서드
    public static CustomKeyboardDetailResponse from(CustomKeyboardWithName entity){
        return CustomKeyboardDetailResponse.builder()
                .customId(entity.id())
                .customName(entity.name())
                .customKeyboardType(entity.layout())
                .housingId(entity.frameId())
                .housingName(entity.frameName())
                .keyCapId(entity.keyCapId())
                .keyCapName(entity.keyCapName())
                .switchId(entity.switchId())
                .switchName(entity.switchName())
                .accessoryId(entity.accessoryId())
                .accessoryName(entity.accessoryName())
                .thumbnail(entity.imageUrl())
                .price(entity.totalPrice())
                .build();
    }

}
