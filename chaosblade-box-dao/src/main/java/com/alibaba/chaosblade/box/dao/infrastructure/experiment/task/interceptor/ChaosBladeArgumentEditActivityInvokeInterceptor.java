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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.google.common.base.Strings;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用来预处理chaosblade相关的参数
 *
 * @author haibin
 */
@Component
@Slf4j
public class ChaosBladeArgumentEditActivityInvokeInterceptor extends BaseActivityInvokeInterceptor {

  private static String HSF = "chaos.hsf";

  private static String APP_NAME = "appname";

  @Override
  public boolean preHandle(
      ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
    String appCode = activityInvokeContext.getExecutableAppCode();
    if (MiniAppUtils.isFromChaosBlade(appCode)
        && PhaseType.ATTACK.equals(activityInvokeContext.getContextData().getPhase())) {
      if (CollectionUtil.isNullOrEmpty(activityInvokeContext.getActivity().getScope())) {
        return true;
      }
      handleHsfScene(activityInvokeContext);
      handleMybatisWhereRule(activityInvokeContext);
    }
    return true;
  }

  private void handleHsfScene(ActivityInvokeContext activityInvokeContext) {
    if (activityInvokeContext.getExecutableAppCode().startsWith(HSF)
        && !MiniAppUtils.isChaosRecovery(activityInvokeContext.getExecutableAppCode())) {
      ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition =
          activityInvokeContext.getActivity().getDefinition().getArguments();
      mapRemoveIf(experimentNodeArgumentsDefinition.getAction(), APP_NAME);
      mapRemoveIf(experimentNodeArgumentsDefinition.getMatcher(), APP_NAME);
    }
  }

  private void handleMybatisWhereRule(ActivityInvokeContext activityInvokeContext) {
    ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition =
        activityInvokeContext.getActivity().getDefinition().getArguments();
    mapRemoveBlank(experimentNodeArgumentsDefinition.getAction());
    mapRemoveBlank(experimentNodeArgumentsDefinition.getMatcher());
  }

  private void mapRemoveIf(Map<String, String> map, String key) {
    if (map == null) {
      return;
    }
    map.remove(key);
  }

  @Override
  public void afterHandle(
      ActivityInvokeContext activityInvokeContext,
      ActivityExecuteResult activityExecuteResult,
      Throwable throwable) {}

  @Override
  public Integer getOrder() {
    return Integer.MAX_VALUE - 3;
  }

  private void mapRemoveBlank(Map<String, String> map) {
    if (map == null) {
      return;
    }
    String value = map.get("where_rule");
    if (!Strings.isNullOrEmpty(value)) {
      map.put("where_rule", value.replaceAll(" ", "").replaceAll("\n", ""));
    }
  }
}
