package site.kikihi.custom.platform.adapter.out.jpa.custom;

import site.kikihi.custom.platform.adapter.out.jpa.BaseTimeEntity;
import site.kikihi.custom.platform.domain.custom.CustomKeyboard;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CustomKeyboardJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    private String frameId;

    private String switchId;

    private String keyCapId;

    private String accessoryId;

    private String name;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CustomKeyboardLayout layout;


    /// from
    public static CustomKeyboardJpaEntity from(CustomKeyboard entity) {
        return CustomKeyboardJpaEntity.builder()
                .userId(entity.getUserId())
                .frameId(entity.getFrameId())
                .switchId(entity.getSwitchId())
                .keyCapId(entity.getKeyCapId())
                .accessoryId(entity.getAccessoryId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .layout(entity.getLayout())
                .build();
    }

    /// toDomain
    public CustomKeyboard toDomain(){
        return CustomKeyboard.builder()
                .id(id)
                .userId(userId)
                .frameId(frameId)
                .switchId(switchId)
                .keyCapId(keyCapId)
                .accessoryId(accessoryId)
                .name(name)
                .imageUrl(imageUrl)
                .layout(layout)
                .build();
    }


    /// 수정하는 함수
    public void update(CustomKeyboard domain) {
        this.id = domain.getId();
        this.userId = domain.getUserId();
        this.frameId = domain.getFrameId();
        this.switchId = domain.getSwitchId();
        this.keyCapId = domain.getKeyCapId();
        this.accessoryId = domain.getAccessoryId();
        this.name = domain.getName();
        this.imageUrl = domain.getImageUrl();
        this.layout = domain.getLayout();
    }

}
