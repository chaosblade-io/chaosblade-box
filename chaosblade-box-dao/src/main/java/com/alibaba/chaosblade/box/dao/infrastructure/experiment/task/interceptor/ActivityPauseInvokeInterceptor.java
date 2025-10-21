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
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.LogFormats;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用来暂停activity
 *
 * @author haibin
 */
@Component
@Slf4j
public class ActivityPauseInvokeInterceptor extends BaseActivityInvokeInterceptor {

  @Override
  public boolean preHandle(
      ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
    Activity activity = activityInvokeContext.getActivity();
    Long before = activity.getBefore();
    if (before != null && before > 0) {
      pause(
          activityInvokeContext,
          LogFormats.Activity.buildPauseBeforeMessage(
              activityInvokeContext.getStepExecuteContext().getActivityTaskId(), before),
          before);
      log.info("pause end,run next handle");
    }
    return true;
  }

  @Override
  public void afterHandle(
      ActivityInvokeContext activityInvokeContext,
      ActivityExecuteResult activityExecuteResult,
      Throwable throwable) {
    Activity activity = activityInvokeContext.getActivity();
    Long after = activity.getAfter();
    if (after != null && after > 0) {
      pause(
          activityInvokeContext,
          LogFormats.Activity.buildPauseAfterMessage(
              activityInvokeContext.getStepExecuteContext().getActivityTaskId(), after),
          after);
      log.info("pause end,run next handle");
    }
  }

  private void pause(ActivityInvokeContext activityInvokeContext, String pauseMessage, long time) {
    if (activityInvokeContext.getContextData().isMetricReload()) {
      return;
    }
    log.info(pauseMessage);
    try {
      TimeUnit.MILLISECONDS.sleep(time);
    } catch (Exception e) {
    }
  }

  @Override
  public Integer getOrder() {
    return Integer.MIN_VALUE + 1;
  }
}
