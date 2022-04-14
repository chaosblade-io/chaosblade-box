package com.alibaba.chaosblade.box.dao.command.task;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskTerminator;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentTaskStopRequest;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 * 
 *
 */
@Component
public class ExperimentTaskStopCommand extends SpringBeanCommand<ExperimentTaskStopRequest, Response<Void>> {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ExperimentChecker experimentChecker;

    @Autowired
    private ActivityTaskTerminator activityTaskTerminator;

    @Override
    public Response<Void> execute(ExperimentTaskStopRequest experimentTaskStopRequest) {
        String experimentTaskId = experimentTaskStopRequest.getTaskId();
        Response<ExperimentTaskDO> response = experimentChecker.assertExperimentTaskExist(experimentTaskId);
        if (!response.isSuccess()) {
            return Response.failedWith(response.getError());
        }
        ExperimentTaskDO experimentTaskDO = response.getResult();
        //如果演练正在停止当中，并且不是因为定时检查发起的，那么不需要重复停止
        if (experimentTaskDO.isStopping() && !experimentTaskStopRequest.isFromScheduler()) {
            return Response.ok();
        }
        ChaosError ChaosError = determineExperimentTaskCanStop(experimentTaskStopRequest, experimentTaskDO);
        if (ChaosError != null) {
            return Response.failedWith(ChaosError);
        }
        //演练任务设置为暂停
        if (!experimentTaskDO.isStopping()) {
            experimentTaskRepository.updateRunState(experimentTaskId, StateEnum.STOPPING);
            //强行终止正在运行的任务
            killRunningActivityTaskIfNeed(experimentTaskDO.getActivityTaskId());
        }
        //执行恢复操作
        recoverTasks(experimentTaskStopRequest, experimentTaskDO);
        //记录
        recordIfNeed(experimentTaskStopRequest, experimentTaskDO);
        return Response.okWithData(null);
    }

    private void recoverTasks(ExperimentTaskStopRequest experimentTaskStopRequest, ExperimentTaskDO experimentTaskDO) {
        ChaosUser chaosUser = experimentTaskStopRequest.getUser();
        ActivityTaskDO activityTaskDO = activityTaskRepository.findFirstRecoveryActivityTask(
            experimentTaskDO.getTaskId());
        if (activityTaskDO != null) {
            ExperimentExecuteContext experimentExecuteContext = new ExperimentExecuteContext(
                ChaosTraceUtil.generateTraceId(), experimentTaskStopRequest.getUser(),
                activityTaskDO, new ExperimentTaskRunnableSettings());
            experimentExecuteContext.getContextData().setUser(chaosUser);
            commandBus.asyncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
        }
    }

    private void recordIfNeed(ExperimentTaskStopRequest experimentTaskStopRequest, ExperimentTaskDO experimentTaskDO) {
    }

    private ChaosError determineExperimentTaskCanStop(ExperimentTaskStopRequest experimentTaskStopRequest,
        ExperimentTaskDO experimentTaskDO) {
        //检查当前节点是否注入中
        //如果有正在注入中的任务
        if (experimentTaskDO.isFinished()) {
            return ChaosError.withCode(CommonErrorCode.B_EXPERIMENT_TASK_ALREADY_FINISHED);
        }
        return null;
    }

    /**
     * 需要终止的活动任务
     *
     * @param activityTaskId
     */
    private void killRunningActivityTaskIfNeed(String activityTaskId) {
        activityTaskRepository.findById(activityTaskId).ifPresent(
            activityTaskDO -> {
                activityTaskTerminator.terminateRunningTask(activityTaskDO);
            });
    }

}
