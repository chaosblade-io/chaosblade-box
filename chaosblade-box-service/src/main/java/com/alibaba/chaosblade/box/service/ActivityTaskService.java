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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ActivityTaskResultConfirmRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ActivityRetryRequest;

/** @author haibin */
public interface ActivityTaskService {

  /**
   * 查找activityTask
   *
   * @param activityTaskId 活动任务ID
   * @return
   */
  Response<ActivityTask> findActivityTaskByActivityTaskId(String activityTaskId);

  /**
   * 重试当前任务
   *
   * @param activityRetryRequest
   * @return
   */
  Response<String> retryActivity(ActivityRetryRequest activityRetryRequest);

  /**
   * 确认活动结果
   *
   * @param activityTaskResultConfirmRequest
   * @return
   */
  Response<Void> confirmActivityTaskResult(
      ChaosUser user, ActivityTaskResultConfirmRequest activityTaskResultConfirmRequest);
}
