package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ActivityExecuteFailedException;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;

/**
 * @author haibin
 *
 *
 */
public interface FlowEngine {

    public void init() throws Exception;

    /**
     * run specified activity in a experiment
     *
     * @param experimentExecuteContext experiment you want to run
     * @return attack result
     */
    public ActivityExecuteResult runActivity(Activity activity, ExperimentExecuteContext experimentExecuteContext)
            throws ActivityExecuteFailedException;

    /**
     * flow engine context
     *
     * @return
     */
    FlowEngineContext getFlowEngineContext();
}
