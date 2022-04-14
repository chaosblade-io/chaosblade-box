package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngineContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
public class StepExecuteContext {

    /**
     * 用户数据
     */
    public StepExecuteContext(FlowEngineContext flowEngineContext, ExperimentExecuteContext experimentExecuteContext) {
        this.flowEngineContext = flowEngineContext;
        this.request = experimentExecuteContext;
    }

    @Getter
    private ExperimentExecuteContext request;

    public PhaseType getPhase() {
        return getChaosAppContext().getPhase();
    }

    @Getter
    private transient FlowEngineContext flowEngineContext;

    public ChaosAppContext getChaosAppContext() {
        return chaosAppContext;
    }

    public void setChaosAppContext(ChaosAppContext chaosAppContext) {
        this.chaosAppContext = chaosAppContext;
    }

    private ChaosAppContext chaosAppContext;

    public String getActivityTaskId() {
        return request.getActivityTaskId();
    }

}
