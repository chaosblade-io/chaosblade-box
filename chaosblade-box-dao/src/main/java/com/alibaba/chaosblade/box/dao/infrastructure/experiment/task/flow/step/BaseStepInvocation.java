package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.Step;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.StepExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngineContext;
import lombok.Getter;

/**
 * @author haibin
 * 
 * 
 */
public abstract class BaseStepInvocation<S extends Step, Result extends StepExecuteResult> {

    @Getter
    protected StepExecuteContext stepExecuteContext;

    protected FlowEngineContext flowEngineContext;

    public BaseStepInvocation(StepExecuteContext stepExecuteContext) {
        this.stepExecuteContext = stepExecuteContext;
        this.flowEngineContext = this.stepExecuteContext.getFlowEngineContext();
    }

    public abstract Result execute(S step);

}
