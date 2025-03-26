package com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FrameFilter implements BaseFilter {

    private String material;
    private String mountType;
    private String soundDampening;
    private Double weight;
    private String layout;
}
