package com.alibaba.chaosblade.platform.metric.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(path = {"/", "/experiment/**","/machine/**", "/scenario/**","/chaostools/**"})
    public String root() {
        return "/index.html";
    }
}
