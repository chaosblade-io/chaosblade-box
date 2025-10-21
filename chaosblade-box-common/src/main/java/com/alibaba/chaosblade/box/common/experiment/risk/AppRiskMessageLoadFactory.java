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

package com.alibaba.chaosblade.box.common.experiment.risk;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Slf4j
@Component
public class AppRiskMessageLoadFactory implements InitializingBean {

  private static final String LOAD_EXACT_PATH = "app-risk/app_risk_message_exact_match.json";

  private static final String LOAD_FUZZY_PATH = "app-risk/app_risk_message_fuzzy_match.json";

  /** 高位小程序警示信息 */
  private final Map<String, AppRiskMessage> appRiskExactMap = new HashMap<>();

  private List<AppRiskMessage> appRiskFuzzyList = new ArrayList<>();

  public AppRiskMessage getAppRiskMessage(String code) {
    return appRiskExactMap.getOrDefault(code, null);
  }

  public List<AppRiskMessage> getAppRiskMessageFuzzy() {
    return appRiskFuzzyList;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    try {
      Type type = new TypeReference<List<AppRiskMessage>>() {}.getType();

      InputStream messageInputStreamExact =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(LOAD_EXACT_PATH);
      List<AppRiskMessage> appRiskMessagesExact = JSON.parseObject(messageInputStreamExact, type);
      if (!CollectionUtil.isNullOrEmpty(appRiskMessagesExact)) {
        appRiskMessagesExact.forEach(
            appRiskMessage -> {
              appRiskExactMap.put(appRiskMessage.getAppCode(), appRiskMessage);
            });
      }

      InputStream messageInputStreamFuzzy =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(LOAD_FUZZY_PATH);
      List<AppRiskMessage> appRiskMessagesFuzzy = JSON.parseObject(messageInputStreamFuzzy, type);
      if (!CollectionUtil.isNullOrEmpty(appRiskMessagesFuzzy)) {
        appRiskFuzzyList = appRiskMessagesFuzzy;
      }
    } catch (Exception e) {
      log.error("[AppRiskMessageLoad] afterPropertiesSet fail ", e);
    }
  }
}
