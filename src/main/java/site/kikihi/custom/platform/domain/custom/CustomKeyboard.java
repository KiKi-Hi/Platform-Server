package site.kikihi.custom.platform.domain.custom;

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

}
