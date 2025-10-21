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

package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.CommandExecutorConstant;
import com.alibaba.chaosblade.box.common.experiment.activity.execute.ActivityTaskFlowExecutionCommandExecutor;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** @author haibin */
@RestController
@RequestMapping(value = "/inner")
public class InnerController {

  @Autowired private CommandBus commandBus;

  @GetMapping(value = "/shutdownActivityTask")
  public RestResponse<Void> shutdownActivityTask(@RequestParam(name = "taskId") String taskId) {
    ActivityTaskFlowExecutionCommandExecutor activityTaskFlowExecutionCommandExecutor =
        (ActivityTaskFlowExecutionCommandExecutor)
            commandBus.select(CommandExecutorConstant.ACTIVITY_TASK_FLOW_EXECUTION);
    activityTaskFlowExecutionCommandExecutor.forceExitCommand(taskId);
    return RestResponseUtil.okWithData(null);
  }

  @GetMapping(value = "/isActivityTaskRunning")
  public RestResponse<Void> existExperimentTask(
      @RequestParam(name = "taskId") String activityTaskId) {
    return RestResponseUtil.okWithData(null);
  }
}
