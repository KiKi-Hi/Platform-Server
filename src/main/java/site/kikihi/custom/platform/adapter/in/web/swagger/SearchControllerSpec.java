package site.kikihi.custom.platform.adapter.in.web.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;
import site.kikihi.custom.global.response.ApiResponse;
import site.kikihi.custom.global.response.page.PageRequest;
import site.kikihi.custom.platform.adapter.in.web.dto.response.product.ProductListResponse;

import java.util.List;

@Tag(name = "검색 API", description = "검색을 위한 API 입니다.")
public interface SearchControllerSpec {

    @Operation(
            summary = "검색 API",
            description = "키워드를 바탕으로 조회합니다."
    )
    ApiResponse<List<ProductListResponse>> searchProducts(
            @RequestParam("keyword") String keyword,
            PageRequest pageRequest,
            @RequestParam(defaultValue = "0.001") float minScore
    );
}
