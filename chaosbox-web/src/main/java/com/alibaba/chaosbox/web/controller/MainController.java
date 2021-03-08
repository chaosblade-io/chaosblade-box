package com.alibaba.chaosbox.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosbox.common.utils.SystemPropertiesUtils;
import com.alibaba.chaosbox.scenario.api.ScenarioRequest;
import com.alibaba.chaosbox.scenario.api.ScenarioYamlProviderStrategy;
import com.alibaba.chaosbox.web.model.SystemInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private ScenarioYamlProviderStrategy scenarioYamlProviderStrategy;

    @Value("${spring.application.version}")
    private String version;

    @GetMapping(path = {"/", "/experiment/**", "/machine/**", "/scenario/**", "/chaostools/**"})
    public String root() {
        return "/index.html";
    }

    @GetMapping("/api/I18n")
    @ResponseBody
    public void i18n(String locale) {
        Locale parseLocale = StringUtils.parseLocale(locale);
        if (parseLocale != null) {
            LocaleContextHolder.setDefaultLocale(parseLocale);
        } else {
            log.warn("i18n api, param is wrong locale");
        }
    }

    @GetMapping("/api/SystemInfo")
    @ResponseBody
    public SystemInfoResponse systemInfo() {
        Locale locale = LocaleContextHolder.getLocale();
        return SystemInfoResponse.builder()
                .locale(locale.toLanguageTag())
                .version(version)
                .build();
    }

    @GetMapping(path = "/api/FetchChaostoolsOverview/**")
    public void overview(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        ScenarioRequest scenarioRequest = parseParam(httpServletRequest, "/api/FetchChaostoolsOverview", "");
        String specYaml = scenarioYamlProviderStrategy.overview(scenarioRequest);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(specYaml.getBytes(SystemPropertiesUtils.getPropertiesFileEncoding()));
    }

    @GetMapping(path = "/api/FetchChaostoolsVersionInfo/**")
    public void versionInfo(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        ScenarioRequest scenarioRequest = parseParam(httpServletRequest, "/api/FetchChaostoolsVersionInfo", "");
        String specYaml = scenarioYamlProviderStrategy.versionYaml(scenarioRequest);

        response.getOutputStream().write(specYaml.getBytes(SystemPropertiesUtils.getPropertiesFileEncoding()));
    }

    @GetMapping(path = "/api/FetchChaostoolsScenarios/**")
    public void tools(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        ScenarioRequest scenarioRequest = parseParam(httpServletRequest, "/api/FetchChaostoolsScenarios", "");
        String specYaml = scenarioYamlProviderStrategy.specYaml(scenarioRequest);

        response.getOutputStream().write(specYaml.getBytes(SystemPropertiesUtils.getPropertiesFileEncoding()));
    }

    @GetMapping("/api/FetchPublicChaostools")
    public void configurationYaml(HttpServletResponse response) throws Exception {
        String specYaml = scenarioYamlProviderStrategy.configuration(ScenarioRequest.builder().build());
        response.getOutputStream().write(specYaml.getBytes(SystemPropertiesUtils.getPropertiesFileEncoding()));
    }

    private ScenarioRequest parseParam(HttpServletRequest httpServletRequest, String replaced, String s) {
        String requestURI = httpServletRequest.getRequestURI();
        String path = requestURI.replace(replaced, s);
        String[] split = StrUtil.split(path, "/");

        ScenarioRequest scenarioRequest = ScenarioRequest.builder().build();
        if (split.length == 2) {
            scenarioRequest.setChaosTools(split[1]);
        }
        if (split.length == 3) {
            scenarioRequest.setChaosTools(split[1]);
            scenarioRequest.setVersion(split[2]);
        }
        if (split.length == 4) {
            scenarioRequest.setChaosTools(split[1]);
            scenarioRequest.setVersion(split[2]);
            scenarioRequest.setSpec(split[3]);
        }

        return scenarioRequest;
    }
}
