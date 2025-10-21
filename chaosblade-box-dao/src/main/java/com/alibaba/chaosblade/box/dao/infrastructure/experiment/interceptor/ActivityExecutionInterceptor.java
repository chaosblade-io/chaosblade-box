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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;

import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskExecutionResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;

/** @author haibin.lhb */
public interface ActivityExecutionInterceptor {

  /**
   * 是否允许运行
   *
   * @param activityTaskDO
   * @param experimentTaskRunnableSettings
   * @return true代表禁止, false代表允许,默认允许
   */
  public boolean forbid(
      ActivityTaskDO activityTaskDO, ExperimentTaskRunnableSettings experimentTaskRunnableSettings);

  /**
   * 开始运行时候
   *
   * @param activityTaskDO
   * @param experimentTaskRunnableSettings
   */
  public void onStarted(
      ActivityTaskDO activityTaskDO, ExperimentTaskRunnableSettings experimentTaskRunnableSettings);

  public void onReturn(
      ActivityTaskDO activityTaskDO,
      ActivityTaskExecutionResponse activityTaskExecutionResponse,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings);

  public void onError(
      ActivityTaskDO activityTaskDO,
      Throwable throwable,
      ExperimentTaskRunnableSettings experimentTaskRunnableSettings);
}
