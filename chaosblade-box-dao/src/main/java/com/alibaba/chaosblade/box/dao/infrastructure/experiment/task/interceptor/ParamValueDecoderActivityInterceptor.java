package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionParameterEncoderAndDecoders;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Component
public class ParamValueDecoderActivityInterceptor extends BaseActivityInvokeInterceptor {

    @Autowired
    private SceneFunctionParameterEncoderAndDecoders sceneFunctionParameterEncoderAndDecoders;

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition = activityInvokeContext.getActivity()
            .getDefinition().getArguments();
        if (experimentNodeArgumentsDefinition == null) { return true; }
        Map<String, String> args = experimentNodeArgumentsDefinition.getAllArguments();
        for (Map.Entry<String, String> entry : args.entrySet()) {
            SceneFunctionParameterComponent sceneFunctionParameterComponent = experimentNodeArgumentsDefinition
                .getArgsComponents().get(entry.getKey());
            String newValue = sceneFunctionParameterEncoderAndDecoders.decode(
                activityInvokeContext.getExecutableAppCode(), entry.getKey(), entry.getValue(),
                sceneFunctionParameterComponent);
            activityInvokeContext.addArgs(entry.getKey(),newValue);
        }
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {

    }
}
