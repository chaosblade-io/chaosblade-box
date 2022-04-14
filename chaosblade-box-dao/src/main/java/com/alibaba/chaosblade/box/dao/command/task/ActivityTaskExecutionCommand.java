package com.alibaba.chaosblade.box.dao.command.task;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.commands.BasePoolCommand;
import com.alibaba.chaosblade.box.common.commands.CommandExecutorConstant;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.activity.cluster.ActivityTaskExecutionResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ActivityInvokeInterruptException;
import com.alibaba.chaosblade.box.common.common.IdentityCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ActivityTaskStartedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ActivityExecutionInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskContextManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngine;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.model.*;
import com.alibaba.chaosblade.box.dao.repository.ActivityRepository;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
public class ActivityTaskExecutionCommand extends BasePoolCommand<ActivityTaskExecutionResponse>
        implements IdentityCommand {

    private static Logger logger = LoggerFactory.getLogger(ActivityTaskExecutionCommand.class);

    @Getter
    private final ExperimentExecuteContext experimentExecuteContext;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentTaskContextManager experimentTaskContextManager;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private FlowEngine flowEngine;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private List<ActivityExecutionInterceptor> activityExecutionInterceptors;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    private final ActivityTaskDO currentActivityTaskDO;

    public ActivityTaskExecutionCommand(
            ExperimentExecuteContext experimentExecuteContext) {
        this.experimentExecuteContext = experimentExecuteContext;
        this.currentActivityTaskDO = experimentExecuteContext.getCurrentActivityTask();
    }

    @Override
    public String getCommandExecutorName() {
        return CommandExecutorConstant.ACTIVITY_TASK_FLOW_EXECUTION;
    }

    @Override
    public ActivityTaskExecutionResponse execute() {
        ActivityTaskExecutionResponse activityTaskExecutionResponse = new ActivityTaskExecutionResponse();
        try {
            //演练上下文需要提前准备
            prepareExperimentExecuteContext(experimentExecuteContext);
            //校验是否可以运行
            if (forbidRun()) {
                activityTaskExecutionResponse.setExecuted(false);
            } else {
                //开始运行之前做一些拦截
                beforeRun();
                ActivityExecuteResult activityExecuteResult = runActivity();
                activityTaskExecutionResponse.setActivityExecuteResult(activityExecuteResult);
            }
        } catch (Throwable throwable) {
            logger.error("execute activity task failed", throwable);
            activityTaskExecutionResponse.setExecuted(true);
            onError(throwable);
        }
        onReturn(activityTaskExecutionResponse);
        return activityTaskExecutionResponse;
    }

    private void onReturn(ActivityTaskExecutionResponse activityTaskExecutionResponse) {
        for (ActivityExecutionInterceptor activityExecutionInterceptor : activityExecutionInterceptors) {
            activityExecutionInterceptor.onReturn(currentActivityTaskDO, activityTaskExecutionResponse,
                    this.experimentExecuteContext.getContextData());
        }
    }

    private void onError(Throwable throwable) {
        for (ActivityExecutionInterceptor activityExecutionInterceptor : activityExecutionInterceptors) {
            activityExecutionInterceptor.onError(currentActivityTaskDO, throwable,
                    this.experimentExecuteContext.getContextData());
        }
    }

    private void beforeRun() {
        for (ActivityExecutionInterceptor activityExecutionInterceptor : activityExecutionInterceptors) {
            activityExecutionInterceptor.onStarted(this.currentActivityTaskDO,
                    this.experimentExecuteContext.getContextData());
        }
    }

    private boolean forbidRun() {
        return activityExecutionInterceptors.stream().anyMatch(
                activityExecutionInterceptor -> activityExecutionInterceptor.forbid(currentActivityTaskDO,
                        experimentExecuteContext.getContextData()));
    }

    private ActivityExecuteResult runActivity() {
        chaosEventDispatcher.fireEvent(new ActivityTaskStartedEvent(this.currentActivityTaskDO));
        try {
            Activity activity = new Activity(experimentExecuteContext.getContextData()
                    .getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY_ID), getActivityHosts(),
                    experimentExecuteContext.getContextData()
                            .getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_TASK_DO,
                                    ActivityTaskDO.class),
                    experimentExecuteContext.getContextData()
                            .getObject(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY_DEFINITION,
                                    ExperimentActivityDefinition.class));
            return flowEngine.runActivity(activity, experimentExecuteContext);
        } catch (Throwable throwable) {
            if (!(throwable instanceof ActivityInvokeInterruptException)) {
                logger.error("run activity error,taskId:{}", this.currentActivityTaskDO.getTaskId(), throwable);
            }
            ActivityExecuteResult activityExecuteResult = new ActivityExecuteResult();
            activityExecuteResult.setError(throwable);
            return activityExecuteResult;
        }
    }

    /**
     * 查找需要重试的机器
     *
     * @param activityTaskDO
     */
    private List<Host> getRetryHosts(ActivityTaskDO activityTaskDO) {
        ActivityRunParam activityRunParam = activityTaskDO.getRunParam();
        if (!CollectionUtil.isNullOrEmpty(activityRunParam.getScope())) {
            List<ExperimentMiniAppTaskDO> experimentMiniAppTaskDOList
                    = activityTargetExecutionResultRepository
                    .findByActivityTaskIdAndResultNotSuccess(activityTaskDO.getTaskId());
            return activityTaskDO.getRunParam().getScope().stream().filter(
                    host -> experimentMiniAppTaskDOList.stream().anyMatch(
                            experimentMiniAppTaskDO -> Objects.equals(experimentMiniAppTaskDO.getHostIp(), host.getIp())))
                    .collect(Collectors.toList());
        }
        return activityRunParam.getScope();
    }

    private List<Host> getActivityHosts() {
        if (this.experimentExecuteContext.getContextData().isRetrying()) {
            return getRetryHosts(this.currentActivityTaskDO);
        }
        return this.currentActivityTaskDO.getRunParam().getScope();
    }

    /**
     * 准备上下文数据
     *
     * @param experimentTaskRunnableSettings
     * @param activityTaskDO
     */
    private void prepareContextData(ExperimentTaskRunnableSettings experimentTaskRunnableSettings,
                                    ActivityTaskDO activityTaskDO) {
        setExperimentTaskDO(experimentTaskRunnableSettings, activityTaskDO);
        processEntryActivityTask(experimentTaskRunnableSettings);
        setActivityRunnableStrategy(experimentTaskRunnableSettings);
        fillActivityInfo(experimentTaskRunnableSettings, activityTaskDO);
    }

    /**
     * 构建活动运行请求
     *
     * @param experimentTaskRunnableSettings
     * @param activityTaskDO
     * @return
     */
    private void fillActivityInfo(
            ExperimentTaskRunnableSettings experimentTaskRunnableSettings,
            ActivityTaskDO activityTaskDO) {
        ExperimentActivityDO experimentActivityDO = activityRepository.findById(activityTaskDO.getActivityId()).get();
        experimentTaskRunnableSettings.setExperimentActivityDO(experimentActivityDO);
        ExperimentActivityDefinition experimentActivityDefinition = experimentActivityDO.getActivityDefinition();
        if (activityTaskDO.getRunParam() != null) {
            overrideActivityDefinition(experimentActivityDefinition, activityTaskDO.getRunParam());
        }
        experimentTaskRunnableSettings.setExperimentActivityDefinition(experimentActivityDefinition);
        experimentTaskRunnableSettings.setPhase(activityTaskDO.getPhase());
    }

    /**
     * 因为运行时候的参数可能和定义活动时候不一样，所以需要覆盖一下
     *
     * @param experimentActivityDefinition
     * @param runParam
     */
    private void overrideActivityDefinition(ExperimentActivityDefinition experimentActivityDefinition,
                                            ActivityRunParam runParam) {
        experimentActivityDefinition.setArguments(runParam.getArguments());
        experimentActivityDefinition.setScope(runParam.getScope());
    }

    private void processEntryActivityTask(ExperimentTaskRunnableSettings settings) {
        ExperimentTaskDO experimentTaskDO = settings.getExperimentTaskDO();
        try {
            settings.setMiniAppContext(
                    experimentTaskContextManager.getExperimentTaskContext(experimentTaskDO.getTaskId())
                            .getMiniAppContext());
        } catch (Exception ex) {
            logger.warn("Set ExperimentTaskContext failed,maybe cause activity run failed", ex);
        }
    }

    /**
     * 目前先默认写死并行
     *
     * @param experimentTaskRunnableSettings
     */
    private void setActivityRunnableStrategy(ExperimentTaskRunnableSettings experimentTaskRunnableSettings) {
        experimentTaskRunnableSettings.enableMiniAppBatchRunning();
    }

    private void setExperimentTaskDO(ExperimentTaskRunnableSettings experimentTaskRunnableSettings,
                                     ActivityTaskDO activityTaskDO) {
        ExperimentTaskDO experimentTaskDO = experimentTaskRunnableSettings.getExperimentTaskDO();
        if (experimentTaskDO == null) {
            experimentTaskDO = experimentTaskRepository.findById(activityTaskDO.getExperimentTaskId()).get();
            experimentTaskRunnableSettings.setExperimentTaskDO(experimentTaskDO);
        }
        experimentTaskRunnableSettings.setNamespace(experimentTaskDO.getNamespace());
        ExperimentDO experimentDO = experimentTaskRunnableSettings.getExperimentDO();
        if (experimentDO == null) {
            experimentDO = experimentRepository.findById(experimentTaskDO.getExperimentId()).get();
            experimentTaskRunnableSettings.setExperimentDO(experimentDO);
        }
        fillUserId(experimentTaskRunnableSettings, experimentTaskDO);
    }

    private void fillUserId(ExperimentTaskRunnableSettings experimentTaskRunnableSettings, ExperimentTaskDO
            experimentTaskDO) {
        if (experimentTaskRunnableSettings.getUserId() == null) {
            experimentTaskRunnableSettings.setUser(
                    new ChaosUser(experimentTaskDO.getUserId()));
        }
    }

    private void prepareExperimentExecuteContext(ExperimentExecuteContext experimentExecuteContext) {
        prepareContextData(experimentExecuteContext.getContextData(), this.currentActivityTaskDO);
    }

    @Override
    public String getIdentity() {
        return this.experimentExecuteContext.getCurrentActivityTask().getExperimentTaskId();
    }

}
