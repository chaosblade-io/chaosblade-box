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

package com.alibaba.chaosblade.box.dao.infrastructure.utils;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/** @author haibin.lhb */
@Slf4j
public class DebugUtils {

  public static void recordSceneFunctionSync(String time, SceneFunctionDO update) {
    List<String> params =
        update.getParameters().stream()
            .map(SceneFunctionParameterDO::getAlias)
            .collect(Collectors.toList());
    ;
    log.info("Debug for check:code:{},{},params:{}", update.getCode(), time, params);
  }
}
