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

package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentTaskStopRequest;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskStopCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 全局自动恢复节点执行命令
 *
 * @author haibin
 */
@Component
@Slf4j
public class ExperimentAutoRecoveryLoadCommand extends BaseExperimentGuardLoadCommand<Response> {

  @Override
  public Response internalExecute(
      ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest) {
    ExperimentTaskDO experimentTaskDO = experimentGuardResultLoadRequest.getExperimentTaskDO();
    if (needRecovery(experimentTaskDO)) {
      ExperimentTaskStopRequest experimentTaskStopRequest = new ExperimentTaskStopRequest();
      experimentTaskStopRequest.setTaskId(experimentTaskDO.getTaskId());
      ChaosUser user = new ChaosUser();
      user.setUserId(experimentTaskDO.getUserId());
      experimentTaskStopRequest.setUser(user);
      experimentTaskStopRequest.setFromScheduler(true);
      commandBus.syncRun(ExperimentTaskStopCommand.class, experimentTaskStopRequest);
      ExperimentGuardInstanceDO experimentGuardInstanceDO =
          experimentGuardResultLoadRequest.getExperimentGuardInstanceDO();
      experimentGuardInstanceDO.setState(GuardRunState.TRIGGERED);
      experimentGuardInstanceDO.setTriggeredReason(
          ExperimentGuardRecoveryLoadCommand.TRIGGER_REASON_TEMPLATE + "自动恢复");
      experimentGuardInstanceRepository.update(experimentGuardInstanceDO);
    }
    return Response.ok();
  }

  private boolean needRecovery(ExperimentTaskDO experimentTaskDO) {
    return (experimentTaskDO.getDuration() > 0)
        && (System.currentTimeMillis() - experimentTaskDO.getGmtCreate().getTime())
            >= (experimentTaskDO.getDuration() * 1000);
  }
}
