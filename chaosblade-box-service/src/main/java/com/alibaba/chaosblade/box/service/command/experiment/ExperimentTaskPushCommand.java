package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.ActivityTaskResultConfirmRequest;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskPushRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.service.command.task.ActivityTaskResultConfirmCommand;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentTaskPushCommand extends SpringBeanCommand<ExperimentTaskPushRequest, Response<Boolean>> {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Override
    public Response<Boolean> execute(ExperimentTaskPushRequest experimentTaskPushRequest) {
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(
            experimentTaskPushRequest.getExperimentTaskId()).orElse(null);
        if (experimentTaskDO == null) { return Response.failedWith(CommonErrorCode.P_EXPERIMENT_TASK_NOT_FOUND); }
        if (experimentTaskDO.isFinished()) {
            return Response.failedWith(CommonErrorCode.B_EXPERIMENT_TASK_ALREADY_FINISHED);
        }
        if (Strings.isNullOrEmpty(experimentTaskDO.getActivityTaskId())) {
            return Response.failedWith(CommonErrorCode.B_EXPERIMENT_TASK_NOT_PUSHABLE);
        }
        ActivityTaskResultConfirmRequest activityTaskResultConfirmRequest = new ActivityTaskResultConfirmRequest();
        activityTaskResultConfirmRequest.setUser(experimentTaskPushRequest.getUser());
        activityTaskResultConfirmRequest.setActivityTaskId(experimentTaskDO.getActivityTaskId());
        activityTaskResultConfirmRequest.setSuccess(true);
        Response<Void> response = commandBus.syncRun(ActivityTaskResultConfirmCommand.class,
            activityTaskResultConfirmRequest);
        if (response.isSuccess()) {
            return Response.okWithData(true);
        }
        return Response.failedWith(response.getError());
    }

}
