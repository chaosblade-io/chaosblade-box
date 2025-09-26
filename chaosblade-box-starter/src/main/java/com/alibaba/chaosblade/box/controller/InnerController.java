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
