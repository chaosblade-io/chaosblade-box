package com.alibaba.chaosblade.platform.metric.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @GetMapping(path = {"/", "/experiment/**", "/machine/**", "/scenario/**", "/chaostools/**"})
    public String root() {
        return "/index.html";
    }

    @GetMapping(path = {
            "/api/FetchChaostoolsOverview/**",
            "/api/FetchChaostoolsVersionInfo/**",
            "/api/FetchChaostoolsScenarios/**"
    })
    public void tools(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();
        int i = StrUtil.indexOf(requestURI, "/", 5, false);

        String substring = "platform/market/chaostools" + requestURI.substring(i);

        HttpRequest request = HttpUtil.createGet("https://chaosblade.oss-cn-hangzhou.aliyuncs.com/" + substring);
        HttpResponse execute = request.execute();
        response.getOutputStream().write(execute.bodyBytes());
    }

    @GetMapping("/api/FetchPublicChaostools")
    public void toolsYaml(HttpServletResponse response) throws Exception {
        HttpRequest request = HttpUtil.createGet("https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/market/chaostools/configuration.yaml");
        HttpResponse execute = request.execute();
        response.getOutputStream().write(execute.bodyBytes());
    }
}
