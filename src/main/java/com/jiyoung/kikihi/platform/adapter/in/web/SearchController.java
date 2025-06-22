package com.jiyoung.kikihi.platform.adapter.in.web;


import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.adapter.out.elasticSearch.ProductESDocument;
import com.jiyoung.kikihi.platform.application.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Profile("prod")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final ElasticSearchService searchService;

    @GetMapping("/phrase")
    public ApiResponse<List<ProductESDocument>> searchByPhrase(
            @RequestParam String phrase
    ) {
        return ApiResponse.ok(searchService.searchByPhrase(phrase));
    }




}

