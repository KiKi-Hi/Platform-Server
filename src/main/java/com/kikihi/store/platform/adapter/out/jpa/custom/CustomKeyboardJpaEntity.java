package com.kikihi.store.platform.adapter.out.jpa.custom;

import com.kikihi.store.platform.adapter.out.jpa.BaseTimeEntity;
import com.kikihi.store.platform.domain.custom.CustomKeyboard;
import com.kikihi.store.platform.domain.custom.CustomKeyboardLayout;
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
                .name(name)
                .imageUrl(imageUrl)
                .layout(layout)
                .build();
    }

}
