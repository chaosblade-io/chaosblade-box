package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step;

import com.alibaba.chaosblade.box.common.experiment.task.flow.BaseRunRequest;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.StepExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngineContext;

/**
 * @author haibin
 *
 *
 */
public abstract class BaseStepRunner<Re extends BaseRunRequest, Rs extends StepExecuteResult> {
    protected FlowEngineContext flowEngineContext;

    public BaseStepRunner(FlowEngineContext flowEngineContext) {
        this.flowEngineContext = flowEngineContext;
    }

    public Rs run(Re re) {
        StepExecuteContext stepExecuteContext = buildContext(re);
        return innerRun(stepExecuteContext);
    }

    protected abstract StepExecuteContext buildContext(Re re);

    protected abstract Rs innerRun(StepExecuteContext context);
}
