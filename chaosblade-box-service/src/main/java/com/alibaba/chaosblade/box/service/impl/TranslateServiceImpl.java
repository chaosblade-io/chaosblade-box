package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.service.TranslateService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TranslateServiceImpl implements TranslateService, InitializingBean {

  private String PATH = "translate/translate.json";

  private Map<String, String> translateJson = new HashMap<>();

  @Override
  public void afterPropertiesSet() throws Exception {
    InputStream inputStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH);
    assert inputStream != null;
    translateJson =
        JSON.parseObject(inputStream, new TypeReference<Map<String, String>>() {}.getType());
  }

  @Override
  public Map<String, String> loadTranslateJson() {
    return translateJson;
  }

  @Override
  public String translateToEn(String cn) {
    if (translateJson.isEmpty()) {
      return "";
    }

    if (translateJson.containsKey(cn)) {
      return translateJson.get(cn);
    }

    return "";
  }
}
