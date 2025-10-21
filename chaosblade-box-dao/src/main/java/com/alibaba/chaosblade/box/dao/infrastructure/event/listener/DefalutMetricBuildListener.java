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

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskStartedEvent;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @ClassName ExperimentCreateEventListener @Author liminghao */
@Component
public class DefalutMetricBuildListener extends BaseChaosEventListener {

  @Autowired private ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

  //    @Autowired
  //    private ExperimentGuardInstanceService experimentGuardInstanceService;

  @Autowired private ExperimentGuardRepository experimentGuardRepository;

  @Autowired private ActivityTaskRepository activityTaskRepository;

  //    @Autowired
  //    private List<GuardInstanceProvider> guardInstanceProviders;

  @Override
  public boolean support(BaseChaosEvent event) {
    return event instanceof ExperimentTaskStartedEvent;
  }

  @Override
  public void onChaosEvent(BaseChaosEvent event) {
    ExperimentTaskStartedEvent experimentTaskStartedEvent = (ExperimentTaskStartedEvent) event;
    addMetric(experimentTaskStartedEvent.getExperimentTaskDO());
  }

  private void addMetric(ExperimentTaskDO experimentTaskDO) {
    List<ActivityTaskDO> activityTaskDOS =
        activityTaskRepository.findByExperimentTaskIdAndPhase(
            experimentTaskDO.getTaskId(), PhaseType.ATTACK);
    List<ExperimentGuardDO> experimentGuardDOS =
        experimentGuardRepository.findByExperimentId(experimentTaskDO.getExperimentId());
    //        for ( ActivityTaskDO activityTaskDO: activityTaskDOS) {
    //            guardInstanceProviders.stream().filter(x -> x.support(activityTaskDO.getAppCode(),
    // experimentGuardDOS))
    //                .forEach( x ->
    // experimentGuardInstanceService.addExperimentGuardInstance(x.provide(experimentTaskDO,
    // activityTaskDO)));
    //        }

  }
}
