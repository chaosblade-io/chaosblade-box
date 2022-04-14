package com.alibaba.chaosblade.box.dao.infrastructure.strategy;


import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin
 *
 * 
 */
public class ActivityTargetRunnableStrategySelector {

    private List<ActivityTargetsRunnableStrategy> targetsRunnableStrategyList = new ArrayList<>();

    private static ActivityTargetsRunnableStrategy Default = new SequenceActivityTargetRunnableStrategy();

    public ActivityTargetRunnableStrategySelector(
        List<ActivityTargetsRunnableStrategy> activityTargetsRunnableStrategies) {
        if (activityTargetsRunnableStrategies != null) {
            targetsRunnableStrategyList = activityTargetsRunnableStrategies;
        }
    }

    public ActivityTargetsRunnableStrategy select(ActivityInvokeContext activityInvokeContext) {
        for (ActivityTargetsRunnableStrategy activityTargetsRunnableStrategy : targetsRunnableStrategyList) {
            if (activityTargetsRunnableStrategy.support(activityInvokeContext)) {
                return activityTargetsRunnableStrategy;
            }
        }
        return Default;
    }

}
