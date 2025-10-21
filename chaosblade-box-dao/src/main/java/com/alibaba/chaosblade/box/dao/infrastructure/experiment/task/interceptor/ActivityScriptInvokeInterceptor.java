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

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.ScriptDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.dao.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ActivityScriptInvokeInterceptor extends BaseActivityInvokeInterceptor {

  @Autowired private SceneFunctionRepository sceneFunctionRepository;

  @Autowired private ScriptRepository scriptRepository;

  @Override
  public boolean preHandle(
      ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
    SceneFunctionDO sceneFunctionDO =
        sceneFunctionRepository.findByCode(activityInvokeContext.getExecutableAppCode()).get();
    Integer functionSource = sceneFunctionDO.getSource();
    if (ChaosFunctionConstant.SOURCE_CUSTOM_APP_SCRIPT.equals(functionSource)) {
      ScriptDO scriptDO = scriptRepository.findByAppCode(activityInvokeContext.getAppCode());
      if (scriptDO != null) {
        activityInvokeContext
            .getTempData()
            .add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_SCRIPT, ScriptDO.toScript(scriptDO));
      }
    }
    return true;
  }

  @Override
  public void afterHandle(
      ActivityInvokeContext activityInvokeContext,
      ActivityExecuteResult activityExecuteResult,
      Throwable throwable) {}
}
