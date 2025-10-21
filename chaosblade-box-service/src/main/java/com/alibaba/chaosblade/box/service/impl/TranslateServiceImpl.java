/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
