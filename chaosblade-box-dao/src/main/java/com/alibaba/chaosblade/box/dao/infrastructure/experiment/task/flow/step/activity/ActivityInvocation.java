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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.BaseStepInvocation;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.StepExecuteContext;
import lombok.extern.slf4j.Slf4j;

/**
 * the atomic invocation to invoke activity
 *
 * @author haibin
 */
@Slf4j
public class ActivityInvocation extends BaseStepInvocation<Activity, ActivityExecuteResult> {

  public ActivityInvocation(StepExecuteContext stepExecuteContext) {
    super(stepExecuteContext);
  }

  /**
   * point功能暂时不支持了
   *
   * @param activity
   * @return
   */
  @Override
  public ActivityExecuteResult execute(Activity activity) {
    ActivityExecuteResult activityExecuteResult =
        new ActivityExecutor(activity, stepExecuteContext).execute();
    if (!activityExecuteResult.isSuccess()) {
      return activityExecuteResult;
    }
    return activityExecuteResult;
  }
}
