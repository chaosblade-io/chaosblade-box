package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentTaskPushInterceptor;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.command.task.ActivityTaskExecutionCommand;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskFinishedCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.base.BaseTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Component
public class ExperimentTaskPusher {

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private List<ExperimentTaskPushInterceptor> experimentTaskPushInterceptors;

    @Autowired
    private ActivityTaskExecutionFinishedProcessor activityTaskExecutionFinishedProcessor;

    public void push(ActivityTaskDO activityTaskDO, ExperimentTaskDO experimentTaskDO,
                     ExperimentTaskRunnableSettings taskRunnableSettings) {
        Preconditions.checkArgument(experimentTaskDO != null, "experimentTaskId Not Null");

        log.info("任务推进检查: currentActivityTaskId={}, phase={}, activityName={}, state={}, result={}, experimentTaskId={}",
                activityTaskDO.getTaskId(),
                activityTaskDO.getPhase(),
                activityTaskDO.getActivityName(),
                activityTaskDO.getState(),
                activityTaskDO.getResult(),
                experimentTaskDO.getTaskId());

        if (ignorePush(activityTaskDO, experimentTaskDO, taskRunnableSettings)) {
            log.info("任务推进被忽略: experimentTaskId={}, currentActivityTaskId={}",
                    experimentTaskDO.getTaskId(), activityTaskDO.getTaskId());
            return;
        }

        ActivityTaskDO nextActivityTaskDO = acquireNextTask(experimentTaskDO,
            activityTaskDO, taskRunnableSettings);
        if (nextActivityTaskDO != null) {
            log.info("推进到下一个活动任务: experimentTaskId={}, currentTaskId={}, nextTaskId={}, nextPhase={}, nextActivityName={}",
                    experimentTaskDO.getTaskId(),
                    activityTaskDO.getTaskId(),
                    nextActivityTaskDO.getTaskId(),
                    nextActivityTaskDO.getPhase(),
                    nextActivityTaskDO.getActivityName());
            executeNextActivityTask(nextActivityTaskDO, taskRunnableSettings);
        } else {
            log.info("没有下一个活动任务，演练任务完成: experimentTaskId={}", experimentTaskDO.getTaskId());
            commandBus.syncRun(ExperimentTaskFinishedCommand.class, experimentTaskDO.getTaskId());
        }
    }

    /**
     * 执行下一个活动任务
     *
     * @param nextActivityTaskDO
     * @param experimentTaskRunnableSettings
     */
    private void executeNextActivityTask(ActivityTaskDO nextActivityTaskDO,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        ChaosUser chaosUser = experimentTaskRunnableSettings.getUser() == null ? new ChaosUser(
            experimentTaskRunnableSettings.getUserId())
            : experimentTaskRunnableSettings.getUser();
        ExperimentExecuteContext newExperimentExecuteContext = new ExperimentExecuteContext(
            experimentTaskRunnableSettings.getRequestId(), chaosUser,
            nextActivityTaskDO, experimentTaskRunnableSettings);
        commandBus.syncRun(new ActivityTaskExecutionCommand(newExperimentExecuteContext));
    }

    private ActivityTaskDO acquireNextTask(ExperimentTaskDO experimentTaskDO, ActivityTaskDO currentActivityTask,
        ExperimentTaskRunnableSettings taskRunnableSettings) {
        String experimentTaskId = experimentTaskDO.getTaskId();

        log.info("获取下一个任务: experimentTaskId={}, currentTaskId={}, currentPhase={}, nextTaskId={}, isStopping={}, isInterrupted={}",
                experimentTaskId,
                currentActivityTask.getTaskId(),
                currentActivityTask.getPhase(),
                currentActivityTask.getNextActivityTaskId(),
                experimentTaskDO.isStopping(),
                taskRunnableSettings.isInterruptedExperimentTaskNow());

        //如果演练已经在停止当中了或者立即停止
        if (experimentTaskDO.isStopping() || taskRunnableSettings.isInterruptedExperimentTaskNow()) {
            //只允许恢复阶段操作
            if (currentActivityTask.inPhase(PhaseType.RECOVER)) {
                ActivityTaskDO nextTask = findNextRecoveryActivityTaskDO(currentActivityTask);
                log.info("演练停止中，查找下一个恢复任务: nextTaskId={}", nextTask != null ? nextTask.getTaskId() : "null");
                return nextTask;
            } else {
                ActivityTaskDO firstRecoveryTask = getFirstRecoveryActivityTask(experimentTaskId);
                log.info("演练停止中，跳转到第一个恢复任务: firstRecoveryTaskId={}", firstRecoveryTask != null ? firstRecoveryTask.getTaskId() : "null");
                return firstRecoveryTask;
            }
        } else {
            ActivityTaskDO nextTask = getActivityTaskByTaskId(currentActivityTask.getNextActivityTaskId());
            log.info("正常推进到下一个任务: nextTaskId={}", nextTask != null ? nextTask.getTaskId() : "null");
            return nextTask;
        }
    }

    private ActivityTaskDO getActivityTaskByTaskId(String taskId) {
        if (taskId == null) { return null; }
        return activityTaskRepository.findById(taskId).orElse(null);
    }

    private ActivityTaskDO getFirstRecoveryActivityTask(String experimentTaskId) {
        return activityTaskRepository.findFirstRecoveryActivityTask(experimentTaskId);
    }

    public ActivityTaskDO findNextRecoveryActivityTaskDO(
        ActivityTaskDO currentActivityTask) {
        return activityTaskRepository.findByExperimentTaskIdAndPhase(
            currentActivityTask.getExperimentTaskId(), PhaseType.RECOVER).stream().filter(
            new Predicate<ActivityTaskDO>() {
                @Override
                public boolean test(ActivityTaskDO activityTaskDO) {
                    return activityTaskDO.getId() > currentActivityTask.getId();
                }
            }).findFirst().orElse(null);
    }

    private boolean ignorePush(ActivityTaskDO activityTaskDO, ExperimentTaskDO experimentTaskDO,
        ExperimentTaskRunnableSettings taskRunnableSettings) {
        return experimentTaskPushInterceptors.stream().anyMatch(new Predicate<ExperimentTaskPushInterceptor>() {
            @Override
            public boolean test(ExperimentTaskPushInterceptor experimentTaskPushInterceptor) {
                return experimentTaskPushInterceptor.ignorePush(activityTaskDO, experimentTaskDO, taskRunnableSettings);
            }
        });
    }

    public void pushByAsyncWay(ExperimentMiniAppTaskDO activityTargetTaskDO) {
        if (!activityTargetTaskDO.isFinished()) {return;}
        String activityTaskId = activityTargetTaskDO.getActivityTaskId();
        try {
            ActivityTaskDO activityTaskDO = activityTaskRepository.findById(
                activityTargetTaskDO.getActivityTaskId()).orElse(null);
            if (activityTaskDO == null || activityTaskDO.isFinished()) {
                return;
            }
            List<ExperimentMiniAppTaskDO> miniAppTaskDOS
                = activityTargetExecutionResultRepository.findByActivityTaskId(activityTaskId);
            boolean allFinished = miniAppTaskDOS
                .stream().allMatch(BaseTaskDO::isFinished);
            //如果所有的机器任务都执行完毕了,就可以推进下一个流程
            if (allFinished) {
                ExperimentTaskRunnableSettings hashMapSettings = new ExperimentTaskRunnableSettings();
                ActivityExecuteResult activityExecuteResult = new ActivityExecuteResult();
                activityExecuteResult.setSuccess(isActivityTaskSuccess(miniAppTaskDOS));
                ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(
                    activityTaskDO.getExperimentTaskId()).orElse(null);
                hashMapSettings.setUser(new ChaosUser(experimentTaskDO.getUserId()));
                hashMapSettings.setExperimentTaskDO(experimentTaskDO);
                activityTaskExecutionFinishedProcessor.afterFinished(activityTaskDO, activityExecuteResult,
                    hashMapSettings);
            }
        } catch (Throwable throwable) {
            log.info("Push experiment task failed,current activity taskId:{}", activityTaskId, throwable);
        }
    }

    private boolean isActivityTaskSuccess(
        List<ExperimentMiniAppTaskDO> experimentMiniAppTaskDOS) {
        return experimentMiniAppTaskDOS.stream().allMatch(
            experimentMiniAppTaskDO -> ResultEnum.SUCCESS.equals(experimentMiniAppTaskDO.getResultEnum()));
    }

}
