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

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskExecutionResponse;
import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityExecutionInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskTerminator;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class ErrorActivityExecutionInterceptor implements ActivityExecutionInterceptor {
  @Autowired private ActivityTaskRepository activityTaskRepository;

  @Autowired private ThrowableChaosErrorWrappers throwableChaosErrorWrappers;

  @Autowired private ActivityTaskTerminator activityTaskTerminator;

  @Override
  public boolean forbid(
      ActivityTaskDO activityTaskDO,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
    return false;
  }

  @Override
  public void onStarted(
      ActivityTaskDO activityTaskDO,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {}

  @Override
  public void onReturn(
      ActivityTaskDO activityTaskDO,
      ActivityTaskExecutionResponse activityTaskExecutionResponse,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {}

  @Override
  public void onError(
      ActivityTaskDO activityTaskDO,
      Throwable throwable,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
    if (!activityTaskDO.isFinished()) {
      activityTaskDO.setResult(ResultEnum.ERROR.getValue());
      activityTaskDO.setErrorMessage(
          throwableChaosErrorWrappers
              .wrapper(throwable, CommonErrorCode.S_ACTIVITY_EXECUTE_FAILED)
              .getErrorMessage());
      activityTaskTerminator.handleActivityTaskAfterTerminate(activityTaskDO);
    }
  }
}
