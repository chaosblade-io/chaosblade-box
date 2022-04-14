package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.CommandExecutorConstant;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskDistributedManager;
import com.alibaba.chaosblade.box.common.experiment.activity.execute.ActivityTaskFlowExecutionCommandExecutor;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.SystemUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ActivityTaskFinishedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class ActivityTaskTerminator {

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private MiniAppTaskManager miniAppTaskManager;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ActivityTaskDistributedManager activityTaskDistributedManager;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    public void terminateRunningTask(ActivityTaskDO activityTaskDO) {
        if (activityTaskDO.isFinished() || activityTaskDO.isReady()) { return;}
        if (activityTaskDO.isRunning()) {
            String serverHostOnRun = activityTaskDO.getHostIp();
            if (serverHostOnRun != null) {
                if (isLocalServer(serverHostOnRun)) {
                    tryKillLocalActivityTask(activityTaskDO.getExperimentTaskId());
                } else {
                    tryKillRemoteActivityTask(serverHostOnRun, activityTaskDO.getExperimentTaskId());
                }
            }
        }
        boolean success = activityTargetExecutionResultRepository.findByActivityTaskId(activityTaskDO.getTaskId())
            .stream().allMatch(
                experimentMiniAppTaskDO -> experimentMiniAppTaskDO.isSuccess() || experimentMiniAppTaskDO.isRejcted());
        activityTaskDO.setResult(success ? ResultEnum.SUCCESS.getValue() : ResultEnum.FAILED.getValue());
        handleActivityTaskAfterTerminate(activityTaskDO);
    }

    private void tryKillLocalActivityTask(String experimentTaskId) {
        ((ActivityTaskFlowExecutionCommandExecutor)commandBus.select(
            CommandExecutorConstant.ACTIVITY_TASK_FLOW_EXECUTION)).forceExitCommand(experimentTaskId);
    }

    private void tryKillRemoteActivityTask(String serverHostOnRun, String experimentTaskId) {
        activityTaskDistributedManager.sendImmediatelyShutdownSignal(serverHostOnRun, experimentTaskId);
    }

    private boolean isLocalServer(String serverHostOnRun) {
        return SystemUtils.getLocalAddress().equals(serverHostOnRun);
    }

    public void handleActivityTaskAfterTerminate(ActivityTaskDO activityTaskDO) {
        Preconditions.checkArgument(activityTaskDO != null && !Strings.isNullOrEmpty(activityTaskDO.getTaskId()),
            "activityTask Required");
        activityTaskDO.setState(StateEnum.FINISHED.getValue());
        activityTaskDO.setGmtEnd(new Date());
        activityTaskRepository.update(activityTaskDO);
        miniAppTaskManager.rejectMiniAppTasksByActivityTaskId(activityTaskDO.getTaskId(),
            activityTaskDO.getErrorMessage());
        chaosEventDispatcher.fireEvent(new ActivityTaskFinishedEvent(activityTaskDO));
    }
}
