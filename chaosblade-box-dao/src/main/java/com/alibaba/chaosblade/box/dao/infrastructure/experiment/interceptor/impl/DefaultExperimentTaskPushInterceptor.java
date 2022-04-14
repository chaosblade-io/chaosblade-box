package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.LogFormats;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentTaskPushInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@Slf4j
public class DefaultExperimentTaskPushInterceptor implements ExperimentTaskPushInterceptor {

    @Override
    public boolean ignorePush(ActivityTaskDO activityTaskDO, ExperimentTaskDO experimentTaskDO,
                              ExperimentTaskRunnableSettings contextData) {
        return !isPushable(activityTaskDO, experimentTaskDO, contextData);
    }

    private boolean isPushable(ActivityTaskDO activityTaskDO, ExperimentTaskDO experimentTaskDO,
        ExperimentTaskRunnableSettings contextData) {
        if (experimentTaskDO == null) {
            return false;
        }
        if (!contextData.isExperimentTaskPushable()) {
            return false;
        }
        if (contextData.isMetricReload()) {
            return false;
        }
        //如果是暂停状态，那么推进
        if (experimentTaskDO.isStopping()) {
            return true;
        }
        if (contextData.isInterruptedExperimentTaskNow()) {
            return true;
        }
        //如果等待用户确认,那么就不执行下一个
        if (UserCheckState.USER_CHECK_STATE_WAITING.getValue().equals(activityTaskDO.getUserCheckState())) {
            log.info(LogFormats.Activity.buildWaitingForUserChecked(activityTaskDO.getTaskId()));
            return false;
        }
        //如果是重试阶段,不执行下一个
        return !contextData.isRetrying();
    }
}
