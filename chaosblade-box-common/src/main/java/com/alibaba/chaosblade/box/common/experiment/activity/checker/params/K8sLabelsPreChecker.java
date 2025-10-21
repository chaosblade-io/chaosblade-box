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

package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.google.common.base.Strings;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class K8sLabelsPreChecker extends ActivityParamPreChecker {

  String Labels = "labels";
  String name = "";

  private static Pattern regrex = Pattern.compile("(\\S+=\\S+,)*(\\S+=\\S+)$");

  @Override
  protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult
      internalCheckArgument(
          ActivityParamPreCheckContext activityParamPreCheckContext,
          ExperimentActivityInfo experimentActivityInfo,
          SceneArgumentDefinition sceneArgumentDefinition) {
    String appCode = experimentActivityInfo.getAppCode();
    if (!MiniAppUtils.isK8S(appCode)) {
      return null;
    }
    String alias = sceneArgumentDefinition.getAlias();
    if (Labels.equals(alias)) {
      String value = sceneArgumentDefinition.getValue();
      if (Strings.isNullOrEmpty(value)) {
        return null;
      }
      if (!regrex.matcher(value).find()) {
        throw new IllegalArgumentException("标签(Labels)输入不合法");
      }
    }
    return null;
  }
}
