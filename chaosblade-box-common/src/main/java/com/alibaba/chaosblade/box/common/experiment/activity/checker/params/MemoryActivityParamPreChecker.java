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
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class MemoryActivityParamPreChecker extends ActivityParamPreChecker {
  @Override
  public List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> preCheck(
      ActivityParamPreCheckContext activityParamPreCheckContext,
      ExperimentActivityInfo experimentActivityInfo) {
    if (MiniAppUtils.isMemLoad(experimentActivityInfo.getAppCode())) {
      return checkArguments(activityParamPreCheckContext, experimentActivityInfo);
    }
    return null;
  }

  @Override
  protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult
      internalCheckArgument(
          ActivityParamPreCheckContext activityParamPreCheckContext,
          ExperimentActivityInfo experimentActivityInfo,
          SceneArgumentDefinition sceneArgumentDefinition) {
    String value = sceneArgumentDefinition.getValue();
    String alias = sceneArgumentDefinition.getAlias();
    if ("mem-percent".equals(alias)) {
      ParamAsserts.assertNumberIn(value, BigDecimal.valueOf(0), BigDecimal.valueOf(100));
    }
    return new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
  }
}
