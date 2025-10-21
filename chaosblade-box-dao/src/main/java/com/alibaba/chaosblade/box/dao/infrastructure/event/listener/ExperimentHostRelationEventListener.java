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

import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ThreadPool;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskFinishedEvent;
import com.alibaba.chaosblade.box.dao.repository.ExperimentHostRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class ExperimentHostRelationEventListener extends BaseChaosEventListener {

  @Autowired private ExperimentHostRelationRepository experimentHostRelationRepository;

  @Autowired private ThreadPool threadPool;

  @Override
  public boolean support(BaseChaosEvent event) {
    return event instanceof ExperimentTaskFinishedEvent;
  }

  @Override
  public void onChaosEvent(BaseChaosEvent event) {
    threadPool
        .executor("host-relation-update")
        .execute(
            new Runnable() {
              @Override
              public void run() {
                String experimentTaskId =
                    ((ExperimentTaskFinishedEvent) event).getExperimentTaskDO().getTaskId();
                StateEnum stateEnum = StateEnum.FINISHED;
                experimentHostRelationRepository.updateTaskState(experimentTaskId, stateEnum);
              }
            });
  }
}
