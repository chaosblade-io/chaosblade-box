package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.activity.task.EnhanceActivityTask;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnumUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ActivityTaskChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.convertor.ArgumentDefinitionConvertor;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityRepository;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class ActivityTaskQueryCommand extends SpringBeanCommand<ActivityTaskQueryRequest, ActivityTask> {

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SceneFunctionRepository sceneFunctionRepository;

    @Autowired
    private MiniAppTaskManager miniAppTaskManager;

    @Autowired
    private ArgumentDefinitionConvertor argumentDefinitionConvertor;

    @Autowired
    private ActivityTaskChecker activityTaskChecker;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Override
    public ActivityTask execute(ActivityTaskQueryRequest activityTaskQueryRequest) {
        ActivityTaskDO activityTaskDO = activityTaskQueryRequest.getActivityTaskDO();
        if (activityTaskDO == null) {
            activityTaskDO = activityTaskRepository.findById(activityTaskQueryRequest.getActivityTaskId()).orElse(null);
        }
        if (activityTaskDO == null) {return null;}
        return loadActivityTask(activityTaskDO, activityTaskQueryRequest);

    }

    private ActivityTask loadActivityTask(ActivityTaskDO activityTaskDO,
                                          ActivityTaskQueryRequest activityTaskQueryRequest) {
        EnhanceActivityTask activityTask = new EnhanceActivityTask();
        String appCode = activityTaskDO.getAppCode();
        Integer order = activityTaskDO.getActivityOrder();
        fillActivityName(activityTask, activityTaskDO);
        activityTask.setExperimentTaskId(activityTaskDO.getExperimentTaskId());
        activityTask.setOrder(order);
        activityTask.setMiniAppCode(appCode);
        activityTask.setExecutableAppCode(activityTaskDO.getExecutableAppCode());
        activityTask.setActivityId(activityTaskDO.getActivityId());
        Date startTime = activityTaskDO.getStartTime() == null ? activityTaskDO.getGmtCreate()
                : activityTaskDO.getStartTime();
        activityTask.setStartTime(startTime);
        activityTask.setEndTime(activityTaskDO.getGmtEnd());
        activityTask.setState(activityTaskDO.getStateEnum());
        activityTask.setRunResult(activityTaskDO.getResultEnum());
        activityTask.setActivityTaskId(activityTaskDO.getTaskId());
        activityTask.setErrorMessage(activityTaskDO.getErrorMessage());
        activityTask.setRunParam(activityTaskDO.getRunParam());
        activityTask.setPhase(activityTaskDO.getPhase());
        activityTask.setFlowId(activityTaskDO.getFlowId());
        activityTask.setUserCheckState(
                EnumUtil.integerValueOf(UserCheckState.class, activityTaskDO.getUserCheckState()));
        if (activityTaskQueryRequest.isReturnMiniAppTasks()) {
            activityTask.setApps(miniAppTaskManager.findMiniAppTasksByActivityTaskId(activityTaskDO.getTaskId()));
            activityTask.setArguments(
                    argumentDefinitionConvertor.splitExperimentNodeArgumentsDefinition(activityTaskDO.getAppCode(),
                            activityTaskDO.getRunParam().getArguments()));
        }
        ExperimentTaskDO experimentTaskDO = activityTaskQueryRequest.getExperimentTaskDO();
        if (experimentTaskDO == null) {
            experimentTaskDO = experimentTaskRepository.findById(activityTaskDO.getExperimentTaskId()).get();
        }
        activityTask.setRetryable(
                activityTaskChecker.isActivityRetryable(experimentTaskDO, activityTaskDO) == null);
        //如果没有运行了，那也就不需要用户确认
        if (!experimentTaskDO.isRunning()) {
            if (!activityTaskDO.userHasChecked()) {
                activityTask.setUserCheckState(null);
            }
        }
        return activityTask;
    }

    private void fillActivityName(ActivityTask activityTask, ActivityTaskDO activityTaskDO) {
        String activityName = activityTaskDO.getActivityName();
        if (Strings.isNullOrEmpty(activityName)) {
            ExperimentActivityDO experimentActivityDO = activityRepository.findLatestActivityByActivityId(
                    activityTaskDO.getActivityId());
            activityName = experimentActivityDO.getActivityName();
            if (Strings.isNullOrEmpty(experimentActivityDO.getActivityName())) {
                Optional<SceneFunctionDO> sceneFunctionDOOptional = sceneFunctionRepository.findByCode(
                        experimentActivityDO.getAppCode());
                if (sceneFunctionDOOptional.isPresent()) {
                    activityName = sceneFunctionDOOptional.get().getName();
                } else {
                    activityName = activityTaskDO.getExecutableAppCode();
                }
            }
        }
        activityTask.setMiniAppName(activityName);
    }
}
