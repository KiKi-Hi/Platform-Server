package com.jiyoung.kikihi.platform.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping
    public String home() {
        return "키키하이 개발서버 입니다!";
    }

}
