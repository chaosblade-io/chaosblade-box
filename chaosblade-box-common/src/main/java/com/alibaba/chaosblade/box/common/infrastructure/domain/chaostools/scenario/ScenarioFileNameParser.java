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

package com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.model.PluginSpecBean;

/** @author yefei */
public class ScenarioFileNameParser {

  public static PluginSpecBean parse(String fileName) {
    String[] split = StrUtil.split(fileName, "-");
    return PluginSpecBean.builder()
        .kind(split[0])
        .type(split[1])
        .version(split[3].replace(".yaml", ""))
        .build();
  }

  public static String toFileName(PluginSpecBean pluginSpecBean) {
    String specFileName =
        String.format(
            "%s-%s-spec-%s.yaml",
            pluginSpecBean.getKind(), pluginSpecBean.getType(), pluginSpecBean.getVersion());
    return specFileName;
  }
}
