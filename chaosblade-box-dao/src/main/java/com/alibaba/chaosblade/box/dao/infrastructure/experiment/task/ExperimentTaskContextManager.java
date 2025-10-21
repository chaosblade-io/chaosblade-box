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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentTaskContextManager {

  @Autowired private ExperimentTaskRepository experimentTaskRepository;

  /**
   * 获取演练的上下文，这个上下文会持久化保存
   *
   * @param taskId
   * @return
   */
  public ExperimentTaskContext getExperimentTaskContext(String taskId) {
    ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(taskId).get();
    if (experimentTaskDO.getExperimentTaskContext() == null) {
      return new ExperimentTaskContext();
    }
    return experimentTaskDO.getExperimentTaskContext();
  }

  public void updateExperimentTaskContext(
      String taskId, ExperimentTaskContext experimentTaskContext) {
    experimentTaskContext = Objects.requireNonNull(experimentTaskContext);
    experimentTaskRepository.updateContext(taskId, experimentTaskContext);
  }
}
