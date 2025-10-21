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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.MiniAppInvokeFlowThreadLocalContext;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.RecordsRepo;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Slf4j
@Component
public class ActivityAsyncCheckCommand extends SpringBeanCommand<AsyncCallBackContext, Boolean> {

  @Autowired private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

  @Autowired private List<ActivityAsyncCallback> activityAsyncCallbackList;

  @Override
  public Boolean execute(AsyncCallBackContext asyncCallBackContext) {
    ExperimentMiniAppTaskDO experimentMiniAppTaskDO =
        asyncCallBackContext.getExperimentMiniAppTaskDO();
    ChaosBladeExpUidDO chaosBladeExpUidDO =
        getChaosBladeExpUidDO(asyncCallBackContext, experimentMiniAppTaskDO);
    if (chaosBladeExpUidDO == null) {
      return false;
    }
    String appCode = chaosBladeExpUidDO.getAppCode();
    doCallback(asyncCallBackContext, appCode);
    return null;
  }

  private void doCallback(AsyncCallBackContext asyncCallBackContext, String appCode) {
    MiniAppInvokeFlowThreadLocalContext.startMiniAppInvoke(
        asyncCallBackContext.getExperimentMiniAppTaskDO(), true);
    try {
      RecordsRepo.getMiniAppInvocationRecorder()
          .log(
              asyncCallBackContext.getExperimentMiniAppTaskDO(),
              "Start handle async callback",
              true);
      activityAsyncCallbackList.forEach(
          activityAsyncCallback -> {
            if (activityAsyncCallback.supportByAppCode(appCode)) {
              activityAsyncCallback.execute(asyncCallBackContext);
            }
          });
    } finally {
      RecordsRepo.getMiniAppInvocationRecorder()
          .log(
              asyncCallBackContext.getExperimentMiniAppTaskDO(), "End handle async callback", true);
      MiniAppInvokeFlowThreadLocalContext.endMiniAppInvoke();
    }
  }

  private ChaosBladeExpUidDO getChaosBladeExpUidDO(
      AsyncCallBackContext asyncCallBackContext, ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
    ChaosBladeExpUidDO chaosBladeExpUidDO = asyncCallBackContext.getChaosBladeExpUidDO();
    if (null == chaosBladeExpUidDO) {
      chaosBladeExpUidDO =
          chaosBladeExpUidRepository.findByActivityTargetTaskId(
              experimentMiniAppTaskDO.getTaskId());
      asyncCallBackContext.setChaosBladeExpUidDO(chaosBladeExpUidDO);
    }
    return chaosBladeExpUidDO;
  }
}
