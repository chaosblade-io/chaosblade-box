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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskExecutionResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityExecutionInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class RetryActivityExecutionInterceptor implements ActivityExecutionInterceptor {
  @Autowired private MiniAppTaskManager miniAppTaskManager;

  @Override
  public boolean forbid(
      ActivityTaskDO activityTaskDO,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
    return false;
  }

  @Override
  public void onStarted(
      ActivityTaskDO activityTaskDO,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
    if (experimentTaskRunnableSettings.isRetrying()) {
      miniAppTaskManager.resetMiniAppTasksByActivityTask(activityTaskDO.getTaskId());
    }
  }

  @Override
  public void onReturn(
      ActivityTaskDO activityTaskDomainWrapper,
      ActivityTaskExecutionResponse activityTaskExecutionResponse,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {}

  @Override
  public void onError(
      ActivityTaskDO activityTaskDomainWrapper,
      Throwable throwable,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {}
}
