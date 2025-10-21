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
import java.util.Map;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class JavaReturnValueMiniAppActivityInvokeInterceptor extends BaseActivityInvokeInterceptor {

  private String JAVA_RETURN_VALUE = "chaos.jvm.return";

  private String FALSE = "false";

  @Override
  public boolean preHandle(
      ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
    String appCode = activityInvokeContext.getExecutableAppCode();
    if (JAVA_RETURN_VALUE.equals(appCode)) {
      Activity activity = activityInvokeContext.getActivity();
      Map<String, String> actions = activity.getDefinition().getArguments().getAction();
      String value = actions.get("value");
      if (FALSE.equals(value)) {
        actions.put("value", "0");
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
