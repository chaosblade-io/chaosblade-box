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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback;

import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentTaskInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import org.springframework.stereotype.Component;

/**
 * 在演练任务创建之前将强弱依赖的标签打上
 *
 * @author haibin.lhb
 */
@Component
public class FeedbackExperimentTaskInterceptor implements ExperimentTaskInterceptor {

  @Override
  public void beforeSaveExperimentTaskDO(
      ExperimentTaskCreateRequest experimentTaskCreateRequest, ExperimentTaskDO experimentTaskDO) {
    ExperimentDO experimentDO = experimentTaskCreateRequest.getExperimentDO();
    ExperimentTaskContext experimentTaskContext = experimentTaskDO.getExperimentTaskContext();
    experimentTaskContext.setOuterId(experimentDO.getOuterId());
    experimentTaskContext.setSource(experimentDO.getSource());
  }
}
