package site.kikihi.custom.platform.domain.product;

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

    private String id; //

    private String name; //

    private String category; //

    private double price; //

    private List<String> description; //

    private String thumbnail; //

    private String mapping;

    private String manufacturer; //

    private String detailPageUrl; //

    private List<Map<String, Object>> options; //

    private Map<String, Object> specTable; // spec_table

    private List<String> allDetailImages; //

}
