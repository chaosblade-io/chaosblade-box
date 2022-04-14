package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnumUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.query.ActivityTaskQuery;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * Author: sunju
 *
 * Date:   2020/5/20
 */
@Component
public class DingtalkAppContextInterceptor extends BaseActivityInvokeInterceptor {

    private static final String EXPERIMENT_TASK = "experiment_task";

    private static final String CURRENT_ACTIVITY_TASK = "current_activity_task";
    private static final String PRE_ACTIVITY_TASK = "pre_activity_task";
    private static final String NEXT_ACTIVITY_TASK = "next_activity_task";

    @Resource
    private ActivityTaskRepository activityTaskRepository;

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        String appCode = activityInvokeContext.getExecutableAppCode();
        if (Objects.equals(appCode, CommonConstant.DINGTALK_APP_CODE_ROBOT_MESSAGE)) {

            ExperimentTaskDO experimentTask = activityInvokeContext.getContextData().getExperimentTaskDO();
            ActivityTaskDO activityTask = activityInvokeContext.getContextData().getActivityTaskDO();

            String preActivityTaskId = activityTask.getPreActivityTaskId();
            String nextActivityTaskId = activityTask.getNextActivityTaskId();

            activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().put(EXPERIMENT_TASK,
                convertExperiment(experimentTask));
            activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().put(CURRENT_ACTIVITY_TASK,
                convertActivity(experimentTask, activityTask));

            ActivityTask preTask = getActivityTask(experimentTask, preActivityTaskId);
            ActivityTask nextTask = getActivityTask(experimentTask, nextActivityTaskId);
            if (null != preTask) {
                activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().put(PRE_ACTIVITY_TASK,
                    preTask);
            }
            if (null != nextTask) {
                activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().put(NEXT_ACTIVITY_TASK,
                    nextTask);
            }
        }
        return true;
    }

    private ActivityTask getActivityTask(ExperimentTaskDO experimentTask, String activityTaskId) {
        if (!Strings.isNullOrEmpty(activityTaskId)) {
            ActivityTaskQuery query = new ActivityTaskQuery();
            query.setTaskId(activityTaskId);
            List<ActivityTaskDO> tasks = activityTaskRepository.find(query);
            if (!CollectionUtil.isNullOrEmpty(tasks)) {
                return convertActivity(experimentTask, tasks.get(0));
            }
        }
        return null;
    }

    private ExperimentTask convertExperiment(ExperimentTaskDO experimentTask) {
        ExperimentTask task = new ExperimentTask();
        task.setExperimentName(experimentTask.getName());
        task.setExperimentId(experimentTask.getExperimentId());
        task.setTaskId(experimentTask.getTaskId());
        task.setState(experimentTask.getStateEnum());
        task.setResult(experimentTask.getResultEnum());
        task.setStartTime(experimentTask.getGmtCreate());
        task.setEndTime(experimentTask.getGmtEnd());
        task.setMessage(experimentTask.getErrorMessage());
        return task;
    }

    private ActivityTask convertActivity(ExperimentTaskDO experimentTask, ActivityTaskDO activityTask) {
        if (null == activityTask) {
            return null;
        }

        ActivityTask task = new ActivityTask();

        task.setMiniAppName(activityTask.getActivityName());
        task.setExperimentTaskId(activityTask.getExperimentTaskId());
        task.setOrder(activityTask.getActivityOrder());
        task.setMiniAppCode(activityTask.getAppCode());
        task.setExecutableAppCode(activityTask.getExecutableAppCode());
        task.setActivityId(activityTask.getActivityId());
        task.setStartTime(
            activityTask.getStartTime() == null ? activityTask.getGmtCreate() : activityTask.getStartTime());
        task.setEndTime(activityTask.getGmtEnd());
        task.setState(activityTask.getStateEnum());
        task.setRunResult(activityTask.getResultEnum());
        task.setActivityTaskId(activityTask.getTaskId());
        task.setErrorMessage(activityTask.getErrorMessage());
        task.setRunParam(activityTask.getRunParam());
        task.setPhase(activityTask.getPhase());
        task.setFlowId(activityTask.getFlowId());
        task.setUserCheckState(EnumUtil.integerValueOf(UserCheckState.class, activityTask.getUserCheckState()));

        if (!experimentTask.isRunning()) {
            if (!activityTask.userHasChecked()) {
                task.setUserCheckState(null);
            }
        }

        return task;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().remove(EXPERIMENT_TASK);
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().remove(CURRENT_ACTIVITY_TASK);
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().remove(PRE_ACTIVITY_TASK);
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().getData().remove(NEXT_ACTIVITY_TASK);
    }

}
