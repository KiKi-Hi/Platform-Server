package com.jiyoung.kikihi.platform.domain.product.keycap;

import lombok.Getter;

@Getter
public enum KeycapMaterial {
    ABS("가볍고 매끄러운 촉감"),
    PBT("내구성이 뛰어나고 단단한 촉감"),
    POM("부드러운 촉감과 탄력 있는 소리"),
    PC("투명한 디자인");

    private String material;

    KeycapMaterial(String material) {
        this.material = material;
    }
}
