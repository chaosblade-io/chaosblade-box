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
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.google.common.base.Strings;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Component
public class SceneDescriptionParser {

  @Autowired
  private SceneFunctionDescriptionMessageLoadFactory sceneFunctionDescriptionMessageLoadFactory;

  public void parseSceneDescription(List<SceneFunctionDO> functions) {
    if (CollectionUtil.isNullOrEmpty(functions)) {
      return;
    }
    functions.forEach(
        sceneFunctionDO -> {
          SceneFunctionDescription description =
              sceneFunctionDescriptionMessageLoadFactory.getSceneAppCodeDescription(
                  sceneFunctionDO.getCode());
          if (null != description) {
            sceneFunctionDO.setDescription(description.getDescription());
            return;
          }
          description =
              sceneFunctionDescriptionMessageLoadFactory.getSceneKeyCodeDescription(
                  getKeyCode(sceneFunctionDO.getCode()));
          if (null != description) {
            sceneFunctionDO.setDescription(description.getDescription());
          }
        });
  }

  private String getKeyCode(String appCode) {
    if (Strings.isNullOrEmpty(appCode)) {
      return appCode;
    }
    String[] strArray = appCode.split("-");
    return strArray[strArray.length - 1];
  }
}
