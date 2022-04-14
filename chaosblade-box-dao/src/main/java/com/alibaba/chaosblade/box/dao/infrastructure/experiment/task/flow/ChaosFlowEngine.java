package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ActivityExecuteFailedException;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;

/**
 * main entrance for chaos,all operations for faultInjection are here
 *
 * @author haibin
 *
 *
 */
public class ChaosFlowEngine implements FlowEngine {

    private FlowEngineContext flowEngineContext;

    /**
     * init a chaos
     *
     * @param flowEngineConfig
     */
    public ChaosFlowEngine(FlowEngineConfig flowEngineConfig) {
        this.flowEngineContext = new FlowEngineContext(flowEngineConfig);
    }

    @Override
    public void init() throws Exception {
        flowEngineContext.init();
    }

    @Override
    public ActivityExecuteResult runActivity(Activity activity, ExperimentExecuteContext experimentExecuteContext)
            throws ActivityExecuteFailedException {
        return this.flowEngineContext.getActivityProcessor().runActivity(activity,experimentExecuteContext);
    }

    @Override
    public FlowEngineContext getFlowEngineContext() {
        return this.flowEngineContext;
    }

}
