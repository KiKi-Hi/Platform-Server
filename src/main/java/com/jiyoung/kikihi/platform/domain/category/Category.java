package com.jiyoung.kikihi.platform.domain.category;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    private Long id;

    private Long parentId;

    private String name;

    private String code;

    private String description;

    // of
    public static Category of(Long parentId, String name, String code, String description) {
        return Category.builder()
                .name(name)
                .parentId(parentId)
                .code(code)
                .description(description)
                .build();
    }

}