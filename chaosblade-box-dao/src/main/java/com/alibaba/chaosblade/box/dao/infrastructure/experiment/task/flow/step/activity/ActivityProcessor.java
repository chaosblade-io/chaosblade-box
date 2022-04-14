package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;

/**
 * activity processor,contains all operations for activity
 *
 * @author haibin
 *
 *
 */
public interface ActivityProcessor {

    /**
     * run activity
     *
     * @param experimentExecuteContext activity request
     * @return result
     */
    public ActivityExecuteResult runActivity(Activity activity, ExperimentExecuteContext experimentExecuteContext);

}
