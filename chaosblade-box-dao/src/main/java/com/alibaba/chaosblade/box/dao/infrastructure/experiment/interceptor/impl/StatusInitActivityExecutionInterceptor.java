package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskExecutionResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.SystemUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityExecutionInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.base.BaseTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author haibin.lhb
 * 
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class StatusInitActivityExecutionInterceptor implements ActivityExecutionInterceptor {

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Override
    public boolean forbid(ActivityTaskDO activityTaskDO,
                          ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        return !allowRun(activityTaskDO, experimentTaskRunnableSettings);
    }

    private boolean allowRun(ActivityTaskDO activityTaskDO,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        if (activityTaskDO.isFinished()) {
            if (experimentTaskRunnableSettings.isRetrying()) {
                return true;
            }
            if (experimentTaskRunnableSettings.supportRepetitionExecution()) {
                return true;
            }
            return experimentTaskRunnableSettings.getExperimentTaskDO().isStopping();
        } else {
            return true;
        }
    }

    @Override
    public void onStarted(ActivityTaskDO activityTaskDO,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        if (!experimentTaskRunnableSettings.supportRepetitionExecution()) {
            updateActivityTaskState(activityTaskDO.getExperimentTaskId(), activityTaskDO.getTaskId());
            //对应的恢复节点需要做调整
            initRecoveryPhase(activityTaskDO);
        }
    }

    /**
     * 开始一个活动任务，修改活动任务状态
     *
     * @param experimentTaskId
     * @param activityTaskId
     */
    private void updateActivityTaskState(String experimentTaskId, String activityTaskId) {
        ActivityTaskDO activityTaskDO = new ActivityTaskDO();
        activityTaskDO.setState(StateEnum.RUNNING.getValue());
        activityTaskDO.setTaskId(activityTaskId);
        activityTaskDO.setHostIp(SystemUtils.getLocalAddress());
        activityTaskDO.setStartTime(new Date());
        activityTaskRepository.update(activityTaskDO);
        experimentTaskRepository.updateActivityTaskId(experimentTaskId, activityTaskDO.getTaskId());
    }

    @Override
    public void onReturn(ActivityTaskDO activityTaskDomainWrapper,
        ActivityTaskExecutionResponse activityTaskExecutionResponse,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {

    }

    @Override
    public void onError(ActivityTaskDO activityTaskDomainWrapper, Throwable throwable,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {

    }

    /**
     * 目前这里会无效,因为Mybatis会过滤掉
     *
     * @param baseTaskDO
     */
    private void initTaskDO(BaseTaskDO baseTaskDO) {
        baseTaskDO.setState(StateEnum.READY.getValue());
        baseTaskDO.setErrorMessage(null);
        baseTaskDO.setResult(null);
        baseTaskDO.setGmtEnd(null);
    }

    private void initRecoveryPhase(ActivityTaskDO activityTaskDO) {
        if (!PhaseType.ATTACK.equals(activityTaskDO.getPhase())) {
            return;
        }
        List<ActivityTaskDO> activityTaskDOS = activityTaskRepository.findByAttackTaskId(activityTaskDO.getTaskId());
        if (activityTaskDOS.isEmpty()) {
            return;
        }
        activityTaskDOS.forEach(activityTaskDO1 -> {
            if (!activityTaskDO1.isFinished()) {
                return;
            }
            initTaskDO(activityTaskDO1);
            activityTaskDO1.setStartTime(null);
            activityTaskRepository.update(activityTaskDO1);
            activityTargetExecutionResultRepository.findByActivityTaskId(activityTaskDO1.getTaskId()).forEach(
                experimentActivityTargetTaskDO -> {
                    if (experimentActivityTargetTaskDO.isFinished()) {
                        experimentActivityTargetTaskDO.setGmtEnd(null);
                        initTaskDO(experimentActivityTargetTaskDO);
                        activityTargetExecutionResultRepository.update(experimentActivityTargetTaskDO);
                    }
                });
        });
    }
}
