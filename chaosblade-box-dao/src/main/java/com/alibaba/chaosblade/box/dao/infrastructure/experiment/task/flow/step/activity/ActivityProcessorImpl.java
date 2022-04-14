package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.BaseStepRunner;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngineContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.StepExecuteContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author haibin
 *
 *
 */
@Slf4j
public class ActivityProcessorImpl implements ActivityProcessor {

    private FlowEngineContext flowEngineContext;

    public ActivityProcessorImpl(
            FlowEngineContext flowEngineContext) {
        this.flowEngineContext = flowEngineContext;
    }

    /**
     * background 不能在这里控制
     *
     * @param experimentExecuteContext
     * @return
     */
    @Override
    public ActivityExecuteResult runActivity(Activity activity, ExperimentExecuteContext experimentExecuteContext) {
        return new BaseStepRunner<ExperimentExecuteContext, ActivityExecuteResult>(
                flowEngineContext) {
            @Override
            protected StepExecuteContext buildContext(ExperimentExecuteContext experimentExecuteContext1) {
                StepExecuteContext stepExecuteContext = new StepExecuteContext(flowEngineContext,
                        experimentExecuteContext);
                ChaosAppContext chaosAppContext = new ChaosAppContext();
                chaosAppContext.setData(experimentExecuteContext.getContextData().getMiniAppContext());
                chaosAppContext.setPhase(
                        experimentExecuteContext.getContextData().getPhase());
                chaosAppContext.setEnvironment(flowEngineContext.getEnvironment());
                chaosAppContext.setSubUserId(experimentExecuteContext.getContextData().getSubUserId());
                chaosAppContext.setStsToken(experimentExecuteContext.getContextData()
                        .getAsString(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_STS_TOKEN));
                chaosAppContext.setUserId(experimentExecuteContext.getContextData().getUserId());
                chaosAppContext.setNamespace(experimentExecuteContext.getContextData().getNamespace());
                stepExecuteContext.setChaosAppContext(chaosAppContext);
                return stepExecuteContext;
            }

            @Override
            protected ActivityExecuteResult innerRun(StepExecuteContext context) {
                ActivityInvocation activityInvocation = new ActivityInvocation(context);
                ActivityExecuteResult activityExecuteResult = activityInvocation.execute(activity);
                activityExecuteResult.setMiniAppContextData(context.getChaosAppContext().getData());
                return activityExecuteResult;
            }
        }.run(experimentExecuteContext);
    }

}
