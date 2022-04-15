package com.alibaba.chaosblade.box.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

    @GetMapping(path = {"/", "/index"})
    public String root() {
        return "/index.html";
    }
}
