package com.jiyoung.kikihi.platform.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private String id;

    private String name;

    private double price;

    private List<String> description;

    private String thumbnail;

    private String manufacturer;

    private String detailPageUrl;

    private String actualPurchaseUrl;

    private String finalPurchaseUrl;

    private List<String> options;

    private List<String> allDetailImages;

}
