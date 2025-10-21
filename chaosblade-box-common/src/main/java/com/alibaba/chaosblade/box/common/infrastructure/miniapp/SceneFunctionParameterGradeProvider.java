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

package com.alibaba.chaosblade.box.common.infrastructure.miniapp;

import com.alibaba.chaosblade.box.common.common.enums.SceneFunctionParameterGradeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Slf4j
@Component
public class SceneFunctionParameterGradeProvider implements InitializingBean {

  /** 全局参数分级配置 */
  protected static Map<String, List<String>> globalParamsGrade = Maps.newConcurrentMap();

  public Map<String, List<String>> getGlobalParamsGradeMap() {
    return globalParamsGrade;
  }

  public SceneFunctionParameterGradeEnum getGradeByCode(String alias) {
    for (String key : globalParamsGrade.keySet()) {
      if (globalParamsGrade.get(key).contains(alias)) {
        return SceneFunctionParameterGradeEnum.parse(key);
      }
    }
    return null;
  }

  private Map<String, List<String>> loadGlobalParamsGrade() {
    /** 全局参数分级配置 */
    String globalParamsGradePath = "config/scene/grade/default_grade.json";
    try (InputStream inputStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(globalParamsGradePath)) {
      assert inputStream != null;
      return JSON.parseObject(
          inputStream, new TypeReference<Map<String, List<String>>>() {}.getType());
    } catch (IOException e) {
      log.error("load function params failed", e);
      return Maps.newConcurrentMap();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    globalParamsGrade = loadGlobalParamsGrade();
  }
}
