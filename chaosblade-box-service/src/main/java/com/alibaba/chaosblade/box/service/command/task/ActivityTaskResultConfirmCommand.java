package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.CommandExecutorConstant;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.ActivityTaskResultConfirmRequest;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTaskStopOption;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.LogFormats;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ActivityTaskChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskPusher;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author haibin
 *
 *
 */
@Component
public class ActivityTaskResultConfirmCommand
    extends SpringBeanCommand<ActivityTaskResultConfirmRequest, Response<Void>> {

    private static Logger logger = LoggerFactory.getLogger("activity-task-confirm");

    @Resource
    private ActivityTaskChecker activityTaskChecker;

    @Resource
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ExperimentTaskManager experimentTaskManager;

    @Resource
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ExperimentTaskPusher experimentTaskPusher;

    @Autowired
    private CommandBus commandBus;

    @Override
    public Response<Void> execute(ActivityTaskResultConfirmRequest activityTaskResultConfirmRequest) {
        String activityTaskId = activityTaskResultConfirmRequest.getActivityTaskId();
        ChaosUser user = activityTaskResultConfirmRequest.getUser();
        ActivityTaskDO activityTaskDO = activityTaskRepository.findById(activityTaskId).orElse(null);
        if (activityTaskDO == null) {
            return Response.failedWith(CommonErrorCode.P_ACTIVITY_TASK_NOT_FOUND);
        }
        if (activityTaskDO.userHasChecked()) {
            return Response.ok();
        }
        Response<Void> activityTaskDOResponse = activityTaskChecker.isActivityTaskCanUserCheck(
            activityTaskDO);
        if (!activityTaskDOResponse.isSuccess()) {
            return Response.failedWith(activityTaskDOResponse.getError());
        }
        UserCheckState userCheckState = activityTaskResultConfirmRequest.isSuccess()
            ? UserCheckState.USER_CHECK_STATE_PASSED
            : UserCheckState.USER_CHECK_STATE_FAILED;
        activityTaskDO.setUserCheckState(userCheckState.getValue());
        boolean success = activityTaskRepository.updateUserCheckState(activityTaskDO, userCheckState);
        if (!success) {
            return Response.okWithData(null);
        }
        logger.info(LogFormats.Activity.buildUserConfirmedMessage(activityTaskId, userCheckState));
        if (UserCheckState.USER_CHECK_STATE_FAILED.equals(userCheckState)) {
            experimentTaskManager.stopTask(user, activityTaskDO.getExperimentTaskId(), new ExperimentTaskStopOption());
        } else {
            asyncPushTask(user, activityTaskDO);
        }
        activityTaskResultConfirmRequest.setExperimentTaskId(activityTaskDO.getExperimentTaskId());
        return Response.okWithData(null);
    }

    private void asyncPushTask(ChaosUser user, ActivityTaskDO activityTaskDO) {
        commandBus.asyncRun(new Command<Void>() {
            @Override
            public String getCommandExecutorName() {
                return CommandExecutorConstant.EXECUTOR_DEFAULT;
            }

            @Override
            public Void execute() {
                ExperimentTaskRunnableSettings contextData = new ExperimentTaskRunnableSettings();
                contextData.setExperimentTaskDO(
                    experimentTaskRepository.findById(activityTaskDO.getExperimentTaskId()).get());
                contextData.setUser(user);
                contextData.setRequestId(ChaosTraceUtil.generateTraceId());
                experimentTaskPusher.push(activityTaskDO, contextData.getExperimentTaskDO(), contextData);
                return null;
            }
        });
    }

}
