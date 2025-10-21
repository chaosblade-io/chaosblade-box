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

package com.alibaba.chaosblade.box.dao.infrastructure.checker;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.runnable.ExperimentRunnableChecker;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExperimentChecker implements InitializingBean {
  @Autowired private ExperimentRepository experimentRepository;

  @Autowired private ExperimentTaskRepository experimentTaskRepository;

  @Autowired private List<ExperimentRunnableChecker> experimentRunnableCheckers;

  public Response<ExperimentDO> assertExperimentExist(String experimentId) {
    Optional<ExperimentDO> chaosExperimentOptional = experimentRepository.findById(experimentId);
    if (!chaosExperimentOptional.isPresent()) {
      return Response.failedWith(ChaosError.withCode(CommonErrorCode.P_EXPERIMENT_NOT_FOUND));
    }
    return Response.okWithData(chaosExperimentOptional.get());
  }

  public Response<ExperimentTaskDO> assertExperimentTaskExist(String taskId) {

    Optional<ExperimentTaskDO> experimentTaskDOOptional = experimentTaskRepository.findById(taskId);
    if (!experimentTaskDOOptional.isPresent()) {
      return Response.failedWith(ChaosError.withCode(CommonErrorCode.P_EXPERIMENT_TASK_NOT_FOUND));
    }
    return Response.okWithData(experimentTaskDOOptional.get());
  }

  public ChaosError checkExperimentRunnable(
      ExperimentRunRequest experimentRunRequest, ExperimentDO experimentDO) {
    ChaosError ChaosError = null;
    for (ExperimentRunnableChecker experimentRunnableChecker : experimentRunnableCheckers) {
      ChaosError = experimentRunnableChecker.checkRunnable(experimentRunRequest, experimentDO);
      if (ChaosError != null) {
        return ChaosError;
      }
    }
    return null;
  }

  public Response<ExperimentDO> assertExperimentExistByIdAndNamespace(
      String experimentId, String namespace) {
    Optional<ExperimentDO> chaosExperimentOptional =
        experimentRepository.findByIdAndNamespace(experimentId, namespace);
    if (!chaosExperimentOptional.isPresent()) {
      return Response.failedWith(ChaosError.withCode(CommonErrorCode.P_EXPERIMENT_NOT_FOUND));
    }
    return Response.okWithData(chaosExperimentOptional.get());
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    AnnotationAwareOrderComparator.sort(experimentRunnableCheckers);
  }
}
