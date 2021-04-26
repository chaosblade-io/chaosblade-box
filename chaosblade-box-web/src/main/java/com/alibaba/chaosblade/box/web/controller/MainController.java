package com.alibaba.chaosblade.box.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.scenario.api.DefaultScenarioYamlProvider;
import com.alibaba.chaosblade.box.web.model.SystemInfoRequest;
import com.alibaba.chaosblade.box.web.model.SystemInfoResponse;
import com.alibaba.chaosblade.box.common.utils.SystemPropertiesUtils;
import com.alibaba.chaosblade.box.scenario.api.ScenarioRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private DefaultScenarioYamlProvider yamlProvider;

    @Value("${spring.application.version}")
    private String version;

    @GetMapping(path = {"/", "/experiment/**", "/machine/**", "/device/**", "/scenario/**",
            "/scene/**", "/chaostools/**", "/tools/**", "/market/**", "/probe/**"})
    public String root() {
        return "/index.html";
    }

    @PostMapping("/api/I18n")
    @ResponseBody
    public void i18n(@RequestBody SystemInfoRequest request) {
        Locale parseLocale = StringUtils.parseLocale(request.getLocale());
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
                .locale(locale.getLanguage())
                .version(version)
                .build();
    }

    @GetMapping(path = "/api/FetchChaostoolsOverview/**")
    public void overview(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        ScenarioRequest scenarioRequest = parseParam(httpServletRequest, "/api/FetchChaostoolsOverview", "");
        String specYaml = yamlProvider.overview(scenarioRequest);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(specYaml.getBytes(SystemPropertiesUtils.getPropertiesFileEncoding()));
    }

    @GetMapping(path = "/api/FetchChaostoolsVersionInfo/**")
    public void versionInfo(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        ScenarioRequest scenarioRequest = parseParam(httpServletRequest, "/api/FetchChaostoolsVersionInfo", "");
        String specYaml = yamlProvider.versionYaml(scenarioRequest);

        response.getOutputStream().write(specYaml.getBytes(SystemPropertiesUtils.getPropertiesFileEncoding()));
    }

    @GetMapping(path = "/api/FetchChaostoolsScenarios/**")
    public void tools(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        ScenarioRequest scenarioRequest = parseParam(httpServletRequest, "/api/FetchChaostoolsScenarios", "");
        String specYaml = yamlProvider.specYaml(scenarioRequest);

        response.getOutputStream().write(specYaml.getBytes(SystemPropertiesUtils.getPropertiesFileEncoding()));
    }

    @GetMapping("/api/FetchPublicChaostools")
    public void configurationYaml(HttpServletResponse response) throws Exception {
        String specYaml = yamlProvider.configuration(ScenarioRequest.builder().build());
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
