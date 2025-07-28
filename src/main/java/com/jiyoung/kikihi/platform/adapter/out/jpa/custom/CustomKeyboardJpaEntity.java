package com.jiyoung.kikihi.platform.adapter.out.jpa.custom;

import com.jiyoung.kikihi.platform.adapter.out.jpa.BaseTimeEntity;
import com.jiyoung.kikihi.platform.domain.custom.CustomKeyboard;
import com.jiyoung.kikihi.platform.domain.custom.CustomKeyboardLayout;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CustomKeyboardJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String frameId;

    private String switchId;

    private String keyCapId;

    private String name;

    private String imageUrl;

    private CustomKeyboardLayout layout;


    /// from
    public static CustomKeyboardJpaEntity from(CustomKeyboard entity) {
        return CustomKeyboardJpaEntity.builder()
                .frameId(entity.getFrameId())
                .switchId(entity.getSwitchId())
                .keyCapId(entity.getKeyCapId())
                .name(entity.getName())
                .imageUrl(entity.getImageUrl())
                .layout(entity.getLayout())
                .build();
    }

    /// toDomain
    private CustomKeyboard toDomain(){
        return CustomKeyboard.builder()
                .id(id)
                .frameId(frameId)
                .switchId(switchId)
                .keyCapId(keyCapId)
                .name(name)
                .imageUrl(imageUrl)
                .layout(layout)
                .build();
    }

}
