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

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.LogFormats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Slf4j
public class ActivityLogInvokeInterceptor extends BaseActivityInvokeInterceptor {

  @Override
  public boolean preHandle(
      ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
    ExperimentExecuteContext experimentExecuteContext =
        activityInvokeContext.getStepExecuteContext().getRequest();
    log.info(
        LogFormats.Activity.buildStartRunActivity(experimentExecuteContext.getActivityTaskId()));
    return true;
  }

  @Override
  public void afterHandle(
      ActivityInvokeContext activityInvokeContext,
      ActivityExecuteResult activityExecuteResult,
      Throwable throwable) {
    ExperimentExecuteContext experimentExecuteContext =
        activityInvokeContext.getStepExecuteContext().getRequest();
    if (throwable != null) {
      log.error(
          "run activity failed,taskId:{}", experimentExecuteContext.getActivityTaskId(), throwable);
      activityExecuteResult.setError(throwable);
    }
    log.info(
        LogFormats.Activity.buildFinishedRunActivity(experimentExecuteContext.getActivityTaskId()));
  }
}
