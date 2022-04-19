package com.alibaba.chaosblade.box.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.DefaultScenarioYamlProvider;
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




    @GetMapping(path = {"/", "/chaos/**", "/machine/**", "/device/**", "/scenario/**",
            "/scene/**", "/chaostools/**", "/tools/**", "/market/**", "/probe/**"})
    public String root() {
        return "/index.html";
    }


}
