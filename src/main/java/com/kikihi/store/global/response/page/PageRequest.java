package com.kikihi.store.global.response.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PageRequest {
    @Builder.Default
    @Schema(description = "페이지 번호 (1부터 시작)", example = "1")
    private int page = 1;

    @Builder.Default
    @Schema(description = "페이지 크기", example = "20")
    private int size = 20;
}
