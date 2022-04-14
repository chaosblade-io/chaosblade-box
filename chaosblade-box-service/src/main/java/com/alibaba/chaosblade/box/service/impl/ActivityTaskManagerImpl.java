package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.SystemUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskQueryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskTerminator;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTasksCreateRequest;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.service.command.task.ActivityTaskBatchCreatedCommand;
import com.alibaba.chaosblade.box.service.command.task.ActivityTaskQueryCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ActivityTaskManagerImpl implements ActivityTaskManager {
    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ActivityTaskTerminator activityTaskTerminator;

    @Override
    public Response<ActivityTask> queryFullActivityTaskInfo(String activityTaskId) {
        ActivityTaskQueryRequest activityTaskQueryRequest = new ActivityTaskQueryRequest(activityTaskId);
        activityTaskQueryRequest.setReturnMiniAppTasks(true);
        ActivityTask activityTask = commandBus.syncRun(ActivityTaskQueryCommand.class,
                activityTaskQueryRequest);
        if (activityTask == null) {
            return Response.failedWith(new ChaosError(CommonErrorCode.P_ACTIVITY_TASK_NOT_FOUND));
        }
        return Response.okWithData(activityTask);
    }

    @Override
    public Response<ActivityTask> querySimpleActivityTaskInfo(ActivityTaskDO activityTaskDO) {
        ActivityTask activityTask = commandBus.syncRun(ActivityTaskQueryCommand.class,
            new ActivityTaskQueryRequest(activityTaskDO));
        if (activityTask == null) {
            return Response.failedWith(new ChaosError(CommonErrorCode.P_ACTIVITY_TASK_NOT_FOUND));
        }
        return Response.okWithData(activityTask);
    }

    @Override
    public void rejectActivityTask(ActivityTaskDO activityTaskDO, String rejectReason) {
        activityTaskDO.setResult(ResultEnum.REJECTED.getValue());
        activityTaskDO.setHostIp(SystemUtils.getLocalAddress());
        activityTaskDO.setErrorMessage(rejectReason);
        activityTaskTerminator.handleActivityTaskAfterTerminate(activityTaskDO);
    }

    @Override
    public long rejectActivityTasksByExperimentTaskId(String experimentTaskId, String rejectJson) {
        return activityTaskRepository.findByExperimentTaskId(experimentTaskId).stream().filter(
            activityTaskDO -> !activityTaskDO.isFinished()).peek(
            activityTaskDO -> rejectActivityTask(activityTaskDO, rejectJson)).count();

    }

    @Override
    public List<ActivityTaskDO> createActivityTasks(ActivityTasksCreateRequest activityTasksCreateRequest) {
        return commandBus.syncRun(ActivityTaskBatchCreatedCommand.class, activityTasksCreateRequest);
    }

}
