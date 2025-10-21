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

import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class DelayTimeOutMaxValuePreCheckContext extends ActivityParamPreChecker {

  private String TIME = "time";

  @Override
  protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult
      internalCheckArgument(
          ActivityParamPreCheckContext activityParamPreCheckContext,
          ExperimentActivityInfo experimentActivityInfo,
          SceneArgumentDefinition sceneArgumentDefinition) {
    String appCode = experimentActivityInfo.getAppCode();
    ChaosBladeAction chaosBladeAction =
        ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode);
    String alias = sceneArgumentDefinition.getAlias();
    if (isDelayAction(chaosBladeAction)) {
      if (alias.equals(TIME)) {
        ParamAsserts.assertNumberRightIn(
            sceneArgumentDefinition.getValue(),
            BigDecimal.valueOf(0),
            BigDecimal.valueOf(270 * 1000));
      }
    }
    return null;
  }

  private boolean isDelayAction(ChaosBladeAction chaosBladeAction) {
    return chaosBladeAction != null && "delay".equals(chaosBladeAction.getAction());
  }
}
