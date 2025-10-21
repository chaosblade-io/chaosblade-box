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

package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.command.task.ActivityTaskExecutionCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ActivityTaskChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ActivityRetryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Slf4j
public class ActivityTaskRetryCommand
    extends SpringBeanCommand<ActivityRetryRequest, Response<String>> {

  @Autowired private ActivityTaskChecker activityTaskChecker;

  @Autowired
  private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

  @Autowired private ExperimentTaskRepository experimentTaskRepository;

  @Override
  public Response<String> execute(ActivityRetryRequest activityRetryRequest) {
    ActivityTaskDO activityTaskDO = activityRetryRequest.getActivityTaskDO();
    ExperimentTaskDO experimentTaskDO =
        experimentTaskRepository.findById(activityTaskDO.getExperimentTaskId()).orElse(null);
    IErrorCode iErrorCode =
        activityTaskChecker.isActivityRetryable(experimentTaskDO, activityTaskDO);
    if (iErrorCode != null) {
      return Response.failedWith(iErrorCode);
    }
    String activityTaskId = activityTaskDO.getTaskId();
    ChaosUser user = activityRetryRequest.getUser();
    ExperimentExecuteContext experimentExecuteContext =
        new ExperimentExecuteContext(
            ChaosTraceUtil.generateTraceId(),
            user,
            activityTaskDO,
            new ExperimentTaskRunnableSettings());
    experimentExecuteContext.getContextData().setRetrying();
    experimentExecuteContext.getContextData().setExperimentTaskDO(experimentTaskDO);
    experimentExecuteContext.getContextData().setExperimentTaskPushableStatus(false);
    setRetryHosts(activityTaskDO);
    commandBus.asyncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
    return Response.okWithData(activityTaskId);
  }

  /**
   * 查找需要重试的机器
   *
   * @param activityTaskDO
   */
  private void setRetryHosts(ActivityTaskDO activityTaskDO) {
    ActivityRunParam activityRunParam = activityTaskDO.getRunParam();
    if (!CollectionUtil.isNullOrEmpty(activityRunParam.getScope())) {
      List<ExperimentMiniAppTaskDO> experimentMiniAppTaskDOList =
          activityTargetExecutionResultRepository.findByActivityTaskIdAndResultNotSuccess(
              activityTaskDO.getTaskId());
      List<Host> retryHosts =
          activityTaskDO.getRunParam().getScope().stream()
              .filter(
                  host ->
                      experimentMiniAppTaskDOList.stream()
                          .anyMatch(
                              experimentMiniAppTaskDO ->
                                  Objects.equals(
                                      experimentMiniAppTaskDO.getHostIp(), host.getIp())))
              .collect(Collectors.toList());
      activityTaskDO.getRunParam().setScope(retryHosts);
    }
  }
}
