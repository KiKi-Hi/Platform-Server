package com.jiyoung.kikihi.platform.domain.product.frame;

import lombok.Getter;

@Getter
public enum MountType {
    TOP_MOUNT("Top Mount"),
    BOTTOM_MOUNT("Bottom Mount"),
    PLATE_MOUNT("Plate Mount"),
    GASKET_MOUNT("Gasket Mount"),
    TRAY_MOUNT("Tray Mount");

    private final String displayName;

    MountType(String displayName) {
        this.displayName = displayName;
    }

}