package com.alibaba.chaosblade.box.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

  @GetMapping(
      path = {
        "/",
        "/login",
        "/chaos/**",
        "/machine/**",
        "/device/**",
        "/scenario/**",
        "/scene/**",
        "/chaostools/**",
        "/tools/**",
        "/market/**",
        "/probe/**"
      },
      produces = "text/html")
  public String root() {
    return "/index";
  }

  @GetMapping(
      path = {"/index"},
      produces = "text/html")
  public String index() {
    return "/index";
  }
}
