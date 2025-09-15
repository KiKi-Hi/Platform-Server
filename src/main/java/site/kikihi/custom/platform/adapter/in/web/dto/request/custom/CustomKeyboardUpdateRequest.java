package site.kikihi.custom.platform.adapter.in.web.dto.request.custom;

import lombok.Data;

@Data
public class CustomKeyboardUpdateRequest {

    private Long id;

    private CustomCategoryType category;

    private String productId;

}
