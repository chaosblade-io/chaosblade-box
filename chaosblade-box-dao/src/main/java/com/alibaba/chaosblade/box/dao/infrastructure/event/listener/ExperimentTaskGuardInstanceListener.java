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

package com.alibaba.chaosblade.box.dao.infrastructure.event.listener;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskFinishedEvent;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentTaskGuardInstanceListener extends BaseChaosEventListener {

  @Autowired private ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

  @Override
  public boolean support(BaseChaosEvent event) {
    return event instanceof ExperimentTaskFinishedEvent;
  }

  @Override
  public void onChaosEvent(BaseChaosEvent event) {
    ExperimentTaskFinishedEvent experimentTaskFinishedEvent = (ExperimentTaskFinishedEvent) event;
    String experimentTaskId = experimentTaskFinishedEvent.getExperimentTaskDO().getTaskId();
    List<ExperimentGuardInstanceDO> experimentGuardInstanceDOS =
        experimentGuardInstanceRepository.findByExperimentTaskId(experimentTaskId);
    for (ExperimentGuardInstanceDO experimentGuardInstanceDO : experimentGuardInstanceDOS) {
      if (!experimentGuardInstanceDO.isHalted()
          && !ExperimentGuardDO.ACTION_TYPE_MONITOR.equals(
              experimentGuardInstanceDO.getActionType())) {
        experimentGuardInstanceDO.setState(GuardRunState.FINISHED);
        experimentGuardInstanceRepository.updateRunningExperimentGuardInstanceDO(
            experimentGuardInstanceDO);
      }
    }
  }
}
