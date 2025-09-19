package site.kikihi.custom.platform.domain.custom;

import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CustomKeyboard extends BaseDomain {

    private Long id;

    private UUID userId;

    private String frameId;

    private String switchId;

    private String keyCapId;

    private String accessoryId;

    private String name;

    private String imageUrl;

    private CustomKeyboardLayout layout;

    /// 정적 팩토리 메서드
    public static CustomKeyboard of(UUID userId, CustomKeyboardLayout layout, String frameId, String switchId, String keyCapId, String accessoryId, String name, String imageUrl) {

        return CustomKeyboard.builder()
                .userId(userId)
                .layout(layout)
                .frameId(frameId)
                .switchId(switchId)
                .keyCapId(keyCapId)
                .accessoryId(accessoryId == null ? "" : accessoryId)
                .name(name)
                .imageUrl(imageUrl)
                .build();
    }

    /// 내부에 존재하하는 것, 삭제하는 함수
    public void removeProduct(String category, String productId) {
        switch (category) {
            case "housing":
                if (this.frameId.equals(productId)) {
                    /// 도메인 내부에 있었다면, null로 바꾸기
                    this.frameId = null;
                } else {
                    throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
                }

                break;
            case "switch":
                if (this.switchId.equals(productId)) {
                    /// 도메인 내부에 있었다면, null로 바꾸기
                    this.switchId = null;
                } else {
                    throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
                }
                break;

            case "keycap":
                if (this.keyCapId.equals(productId)) {
                    /// 도메인 내부에 있었다면, null로 바꾸기
                    this.keyCapId = null;
                } else {
                    throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
                }
                break;

            case "accessory":
                if (this.accessoryId.equals(productId)) {
                    /// 도메인 내부에 있었다면, null로 바꾸기
                    this.accessoryId = null;
                } else {
                    throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
                }
                break;

            default:
                throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
        }

    }

    /// 내부에 존재하하는 것, 삭제하는 함수
    public void addProduct(String category, String productId) {

        switch (category) {
            case "housing":
                if (this.frameId == null) {
                    /// 도메인 내부에 없었다면, 새로 추가하기
                    this.frameId = productId;
                } else {
                    throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
                }
                break;
            case "switch":
                if (this.switchId == null) {
                    /// 도메인 내부에 없었다면, 새로 추가하기
                    this.switchId = productId;
                } else {
                    throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
                }
                break;

            case "keycap":
                if (this.keyCapId.equals(productId)) {
                    /// 도메인 내부에 있었다면, null로 바꾸기
                    this.keyCapId = null;
                } else {
                    throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
                }
                break;

            case "accessory":
                if (this.accessoryId == null) {
                    /// 도메인 내부에 있었다면, null로 바꾸기
                    this.accessoryId = productId;
                } else {
                    throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
                }
                break;

            default:
                throw new IllegalStateException(ErrorCode.BAD_PRODUCT_DELETE_CUSTOM.getMessage());
        }

    }

}
