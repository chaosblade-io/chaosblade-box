package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.LogFormats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 用来暂停activity
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ActivityPauseInvokeInterceptor extends BaseActivityInvokeInterceptor {

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        Activity activity = activityInvokeContext.getActivity();
        Long before = activity.getBefore();
        if (before != null && before > 0) {
            pause(activityInvokeContext,
                LogFormats.Activity
                    .buildPauseBeforeMessage(activityInvokeContext.getStepExecuteContext().getActivityTaskId(), before),
                before);
            log.info("pause end,run next handle");
        }
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {
        Activity activity = activityInvokeContext.getActivity();
        Long after = activity.getAfter();
        if (after != null && after > 0) {
            pause(activityInvokeContext,
                LogFormats.Activity
                    .buildPauseAfterMessage(activityInvokeContext.getStepExecuteContext().getActivityTaskId(), after),
                after);
            log.info("pause end,run next handle");
        }
    }

    private void pause(ActivityInvokeContext activityInvokeContext, String pauseMessage, long time) {
        if (activityInvokeContext.getContextData().isMetricReload()) { return; }
        log.info(pauseMessage);
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (Exception e) {
        }
    }

    @Override
    public Integer getOrder() {
        return Integer.MIN_VALUE + 1;
    }
}
