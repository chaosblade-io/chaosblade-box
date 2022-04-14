package com.alibaba.chaosblade.box.dao.infrastructure.strategy;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.config.DefaultChaosProperties;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
@Order(1000)
public class DynamicBatchActivityTargetRunnableStrategy extends BatchActivityTargetRunnableStrategy {

    @Autowired
    private DefaultChaosProperties chaosProperties;

    @Override
    public boolean support(ActivityInvokeContext activityInvokeContext) {
        return ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_ACTIVITY_RUNNABLE_STRATEGY_BATCH.equals(
            activityInvokeContext.getContextData()
                .getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_STRATEGY))
            && !activityInvokeContext.getContextData()
            .containsKey(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_BATCH_SIZE);
    }

    @Override
    protected int calcBatchSize(ActivityInvokeContext activityInvokeContext) {
        List<Host> hostList = activityInvokeContext.getActivity().getScope();
        return Math.min(hostList.size(), chaosProperties.getMiniAppMaxBatchSize());
    }

}
