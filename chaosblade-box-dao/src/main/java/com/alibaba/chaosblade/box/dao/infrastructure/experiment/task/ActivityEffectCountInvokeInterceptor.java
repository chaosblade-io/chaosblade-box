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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseActivityInvokeInterceptor;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ActivityEffectCountInvokeInterceptor extends BaseActivityInvokeInterceptor {

  private static String EFFECT_COUNT = "effect-count";

  private static String EFFECT_PERCENT = "effect-percent";

  private static String EVICT_COUNT = "evict-count";

  private static String EVICT_PERCENT = "evict-percent";

  @Override
  public boolean preHandle(
      ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
    ExperimentActivityDefinition experimentActivityDefinition =
        activityInvokeContext.getActivity().getDefinition();
    if (MiniAppUtils.isFromChaosBlade(activityInvokeContext.getAppCode())
        && experimentActivityDefinition.getArguments() != null) {
      removeKeyIfIlleagl(experimentActivityDefinition.getArguments(), EFFECT_COUNT);
      removeKeyIfIlleagl(experimentActivityDefinition.getArguments(), EFFECT_PERCENT);
      removeKeyIfIlleagl(experimentActivityDefinition.getArguments(), EVICT_COUNT);
      removeKeyIfIlleagl(experimentActivityDefinition.getArguments(), EVICT_PERCENT);
    }
    return true;
  }

  @Override
  public void afterHandle(
      ActivityInvokeContext activityInvokeContext,
      ActivityExecuteResult activityExecuteResult,
      Throwable throwable) {}

  private void removeKeyIfIlleagl(
      ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition, String key) {
    if (experimentNodeArgumentsDefinition.getAllArguments().containsKey(key)) {
      String value = experimentNodeArgumentsDefinition.getAllArguments().get(key);
      try {
        if (Integer.valueOf(value) <= 0) {
          experimentNodeArgumentsDefinition.removeArg(key);
        }
      } catch (Exception ex) {

      }
    }
  }
}
