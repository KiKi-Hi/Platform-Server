package com.jiyoung.kikihi.platform.domain.product.frame;

import lombok.Getter;

@Getter
public enum MountType {
    TOP("Top Mount"),
    BOTTOM("Bottom Mount"),
    PLATE("Plate Mount"),
    GASKET("Gasket Mount"),
    TRAY("Tray Mount");

    private final String displayName;

    MountType(String displayName) {
        this.displayName = displayName;
    }

}