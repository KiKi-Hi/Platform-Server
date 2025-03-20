package com.jiyoung.kikihi.platform.domain;


import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
public abstract class BaseDomain {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
