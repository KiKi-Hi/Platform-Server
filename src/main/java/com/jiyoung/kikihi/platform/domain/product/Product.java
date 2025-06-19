package com.jiyoung.kikihi.platform.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;

    private Long categoryId;

    private String name;

    private double price;

    private String thumbnail;

    private String detailPageUrl;

    private String finalRedirectUrl;

    private String finalPurchaseUrl;

    private String text;

    private List<String> optionList;

    private List<String> detailImageList;

    private List<String> specList;


}
