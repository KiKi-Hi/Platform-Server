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


    // of
    public static Category of(Long parentId, String name) {
        return Category.builder()
                .name(name)
                .parentId(parentId)
                .build();
    }

}