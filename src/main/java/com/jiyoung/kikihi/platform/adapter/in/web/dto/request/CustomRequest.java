package com.jiyoung.kikihi.platform.adapter.in.web.dto.request;

import lombok.Data;

@Data
public class CustomRequest {

    private Long frameId;
    private Long switchId;
    private Long keycapId;
    private String name;
    private double price;
    private String image_url;



}
