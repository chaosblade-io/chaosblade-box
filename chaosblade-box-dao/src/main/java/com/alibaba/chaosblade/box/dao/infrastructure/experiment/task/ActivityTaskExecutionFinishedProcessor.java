package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityTaskExecutionFinishedInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityRepository;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

//import com.google.common.base.Predicate;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@Slf4j
public class ActivityTaskExecutionFinishedProcessor {

    @Autowired
    private ExperimentTaskContextManager experimentTaskContextManager;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private ActivityTaskTerminator activityTaskTerminator;

    @Autowired
    private ExperimentTaskPusher experimentTaskPusher;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private List<ActivityTaskExecutionFinishedInterceptor>
        activityTaskExecutionFinishedInterceptors;

    public void afterFinished(ActivityTaskDO activityTaskDO, ActivityExecuteResult activityExecuteResult,
                              ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        if (ignoreFinishedEvent(activityTaskDO, activityExecuteResult, experimentTaskRunnableSettings)) { return; }
        if (experimentTaskRunnableSettings.getExperimentTaskDO() == null) {
            experimentTaskRunnableSettings.setExperimentTaskDO(
                experimentTaskRepository.findById(activityTaskDO.getExperimentTaskId()).orElse(null));
        }
        setUserCheckState(activityTaskDO);
        saveExperimentTaskContext(activityTaskDO, activityExecuteResult);
        setActivityResult(activityTaskDO, activityExecuteResult);
        activityTaskTerminator.handleActivityTaskAfterTerminate(activityTaskDO);
        if (!activityTaskDO.isSuccess() && activityTaskDO.getRunParam().isInterruptedIfFailed()) {
            experimentTaskRunnableSettings.setInterruptedExperimentTaskNow();
        }
        experimentTaskPusher.push(activityTaskDO, experimentTaskRunnableSettings.getExperimentTaskDO(),
            experimentTaskRunnableSettings);
    }

    private boolean ignoreFinishedEvent(ActivityTaskDO activityTaskDO, ActivityExecuteResult activityExecuteResult,
        ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        return activityTaskExecutionFinishedInterceptors.stream().anyMatch(
            (Predicate<ActivityTaskExecutionFinishedInterceptor>)input -> input
                .ignoreFinished(activityTaskDO, activityExecuteResult, experimentTaskRunnableSettings));
    }

    /**
     * 设置节点结果
     *
     * @param activityTaskDO
     * @param activityExecuteResult
     */
    private void setActivityResult(ActivityTaskDO activityTaskDO, ActivityExecuteResult activityExecuteResult) {
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        ActivityRunParam activityRunParam = activityTaskDO.getRunParam();
        Integer failedTaskCount = activityTargetExecutionResultRepository.countFailedTask(activityTaskDO.getTaskId());
        if (failedTaskCount > 0) {
            //节点容忍度判断
            int tolerance = activityRunParam.getFailedTolerance();
            if (tolerance <= 0) {
                resultEnum = ResultEnum.FAILED;
            } else {
                Integer totalTaskCount = activityTargetExecutionResultRepository.countTotalTask(
                    activityTaskDO.getTaskId());
                if (totalTaskCount > 0) {
                    if (Float.parseFloat(failedTaskCount + "") / Float.parseFloat(totalTaskCount + "") * 100
                        >= tolerance) {
                        resultEnum = ResultEnum.FAILED;
                    }
                } else {
                    log.warn("activityTask:{} has not subTask", activityTaskDO.getTaskId());
                }
            }
        }
        activityTaskDO.setResult(resultEnum.getValue());
        if (activityExecuteResult.getError() != null) {
            activityTaskDO.setErrorMessage(ExceptionHelper.detailedMessage(activityExecuteResult.getError()));
        }
        if (!Strings.isNullOrEmpty(activityExecuteResult.getErrorMessage())) {
            activityTaskDO.setErrorMessage(activityExecuteResult.getErrorMessage());
        }
    }

    private void setUserCheckState(ActivityTaskDO activityTaskDO) {
        if (waitingUserChecked(activityTaskDO)) {
            activityTaskDO.setUserCheckState(UserCheckState.USER_CHECK_STATE_WAITING.getValue());
        }
    }

    protected boolean waitingUserChecked(ActivityTaskDO activityTaskDO) {
        ExperimentActivityDO experimentActivityDO = activityRepository.findById(activityTaskDO.getActivityId()).get();
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(activityTaskDO.getExperimentTaskId())
            .get();
        return !experimentTaskDO.isStopping() && experimentActivityDO.needUserCheck()
            && activityTaskDO.getUserCheckState() == null;
    }

    private void saveExperimentTaskContext(ActivityTaskDO activityTaskDO, ActivityExecuteResult activityExecuteResult) {
        try {
            ExperimentTaskContext experimentTaskContext = experimentTaskContextManager.getExperimentTaskContext(
                activityTaskDO.getExperimentTaskId());
            experimentTaskContext.setMiniAppContext(activityExecuteResult.getMiniAppContextData());
            experimentTaskContextManager.updateExperimentTaskContext(activityTaskDO.getExperimentTaskId(),
                experimentTaskContext);
        } catch (Exception ex) {
            log.warn("Save ExperimentTaskContext failed", ex);
        }
    }

    public static void main(String[] args) {
        System.out.println(Float.parseFloat(22 + "") / Float.parseFloat(100 + ""));
    }

}
