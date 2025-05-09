package com.jiyoung.kikihi.platform.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "사용자") String name, Model model) {
        model.addAttribute("message", "안녕하세요, " + name + "님!");
        return "hello";
    }
}
