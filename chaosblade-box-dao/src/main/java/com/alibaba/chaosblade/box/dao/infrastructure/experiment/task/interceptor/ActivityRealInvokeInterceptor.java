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

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Slf4j
public class ActivityRealInvokeInterceptor extends BaseActivityInvokeInterceptor {

  @Override
  public boolean preHandle(
      ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
    return true;
  }

  @Override
  public void postHandle(
      OnceInvoke<ActivityInvokeContext, ActivityExecuteResult> onceInvoke,
      ActivityInvokeContext activityInvokeContext,
      ActivityExecuteResult activityExecuteResult) {
    ActivityExecuteResult result = onceInvoke.invoke(activityInvokeContext);
    activityExecuteResult.setMiniAppContextData(result.getMiniAppContextData());
    activityExecuteResult.setError(result.getError());
    activityExecuteResult.setAppResponses(result.getAppResponses());
    activityExecuteResult.setAppCode(result.getAppCode());
    activityExecuteResult.setSuccess(result.isSuccess());
  }

  @Override
  public void afterHandle(
      ActivityInvokeContext activityInvokeContext,
      ActivityExecuteResult activityExecuteResult,
      Throwable throwable) {}

  @Override
  public Integer getOrder() {
    return Integer.MIN_VALUE + 5;
  }
}
