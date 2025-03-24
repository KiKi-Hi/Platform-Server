package com.jiyoung.kikihi.platform.domain.product.frame;

import lombok.Getter;

@Getter
public enum Material {
    ALUMINUM("알루미늄"),
    POLYCARBONATE("폴리카보네이트(PC)"),
    POM("POM");

    private final String displayName;

    Material(String displayName) {
        this.displayName = displayName;
    }

}