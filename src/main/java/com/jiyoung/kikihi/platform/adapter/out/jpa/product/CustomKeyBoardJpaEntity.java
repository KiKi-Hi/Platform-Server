package com.jiyoung.kikihi.platform.adapter.out.jpa.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiyoung.kikihi.platform.adapter.out.jpa.BaseEntity;
import com.jiyoung.kikihi.platform.domain.product.CustomKeyboard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "custom")
public class CustomKeyBoardJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 생성 추가
    private Long id;

    @JsonProperty("frameId")
    private Long frameId;

    @JsonProperty("switchId")
    private Long switchId;

    @JsonProperty("keycapId")
    private Long keycapId;
    private String name;
    private double price;
    private String image_url;

    public static CustomKeyBoardJpaEntity from(CustomKeyboard customKeyboard) {
        return CustomKeyBoardJpaEntity.builder()
                .frameId(customKeyboard.getFrameId())
                .switchId(customKeyboard.getSwitchId())
                .keycapId(customKeyboard.getKeycapId())
                .name(customKeyboard.getName())
                .price(customKeyboard.getPrice())
                .image_url(customKeyboard.getImage_url())
                .build();

    }

    public CustomKeyboard toDomain(){
        return CustomKeyboard.builder()
                .frameId(this.frameId)
                .switchId(this.switchId)
                .keycapId(this.keycapId)
                .name(this.name)
                .price(this.price)
                .image_url(this.image_url)
                .build();
    }

}
