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

package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ActivityTaskResultConfirmRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ActivityRetryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskManager;
import com.alibaba.chaosblade.box.service.ActivityTaskService;
import com.alibaba.chaosblade.box.service.command.task.ActivityTaskResultConfirmCommand;
import com.alibaba.chaosblade.box.service.command.task.ActivityTaskRetryCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Slf4j
public class ActivityTaskServiceImpl implements ActivityTaskService {

  @Autowired private ActivityTaskManager activityTaskManager;

  @Autowired private CommandBus commandBus;

  @Override
  public Response<ActivityTask> findActivityTaskByActivityTaskId(String activityTaskId) {
    return activityTaskManager.queryFullActivityTaskInfo(activityTaskId);
  }

  @Override
  public Response<String> retryActivity(ActivityRetryRequest activityRetryRequest) {
    return commandBus.syncRun(ActivityTaskRetryCommand.class, activityRetryRequest);
  }

  @Override
  public Response<Void> confirmActivityTaskResult(
      ChaosUser user, ActivityTaskResultConfirmRequest activityTaskResultConfirmRequest) {
    return commandBus.syncRun(
        ActivityTaskResultConfirmCommand.class, activityTaskResultConfirmRequest);
  }
}
