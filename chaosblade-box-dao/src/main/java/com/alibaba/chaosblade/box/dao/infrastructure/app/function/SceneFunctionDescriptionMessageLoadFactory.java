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

package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Slf4j
@Component
public class SceneFunctionDescriptionMessageLoadFactory implements InitializingBean {

  private static final String LOAD_PATH = "config/scene/description/scene_description.json";

  /** map key为小程序的appCode */
  private final Map<String, SceneFunctionDescription> sceneAppCodeDescription = new HashMap<>();

  /** map key为小程序类别 */
  private final Map<String, SceneFunctionDescription> sceneKeyCodeDescription = new HashMap<>();

  public SceneFunctionDescription getSceneAppCodeDescription(String code) {
    return sceneAppCodeDescription.getOrDefault(code, null);
  }

  public SceneFunctionDescription getSceneKeyCodeDescription(String code) {
    return sceneKeyCodeDescription.getOrDefault(code, null);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    try {
      InputStream inputstream =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(LOAD_PATH);
      Type type = new TypeReference<List<SceneFunctionDescription>>() {}.getType();
      List<SceneFunctionDescription> sceneFunctionDescriptions =
          JSON.parseObject(inputstream, type);
      if (!CollectionUtil.isNullOrEmpty(sceneFunctionDescriptions)) {
        sceneFunctionDescriptions.forEach(
            sceneFunctionDescription -> {
              sceneAppCodeDescription.put(
                  sceneFunctionDescription.getAppCode(), sceneFunctionDescription);
              if (!Strings.isNullOrEmpty(sceneFunctionDescription.getKeyCode())) {
                sceneKeyCodeDescription.put(
                    sceneFunctionDescription.getKeyCode(), sceneFunctionDescription);
              }
            });
      }
    } catch (Exception e) {
      log.error("[SceneFunctionDescriptionMessageLoad] afterPropertiesSet fail ", e);
    }
  }
}
