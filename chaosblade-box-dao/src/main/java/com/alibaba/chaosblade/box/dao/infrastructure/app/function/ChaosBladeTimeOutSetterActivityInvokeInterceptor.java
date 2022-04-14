package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.activity.checker.params.TimeOutMaxValuePreCheckContext;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseActivityInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class ChaosBladeTimeOutSetterActivityInvokeInterceptor extends BaseActivityInvokeInterceptor {

    public static String param = "timeout";

    public static String param_litmus = "TOTAL_CHAOS_DURATION";

    private int offsetSecond = 5;

    @Autowired
    private SceneFunctionParameterRepository sceneFunctionParameterRepository;

    @Autowired
    private SceneFunctionRepository sceneFunctionRepository;

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        ExperimentTaskDO experimentTaskDO = activityInvokeContext.getContextData().getExperimentTaskDO();
        if (MiniAppUtils.isFromChaosBlade(activityInvokeContext.getAppCode()) ||
                MiniAppUtils.isFromLitmusChaos(activityInvokeContext.getAppCode())) {
            SceneFunctionDO sceneFunctionDO = sceneFunctionRepository.findByCode(activityInvokeContext.getAppCode())
                .orElse(null);
            if (sceneFunctionDO == null) { return true; }
            if (sceneFunctionDO.getCode().startsWith("litmuschaos")) {
                if (sceneFunctionParameterRepository.existParam(sceneFunctionDO.getFunctionId(), param_litmus)) {
                    Long duration = experimentTaskDO.getDuration();
                    if (duration != null && duration > 0) {
                        duration = duration > TimeOutMaxValuePreCheckContext.MAX_TIME_OUT ?
                                TimeOutMaxValuePreCheckContext.MAX_TIME_OUT : duration;
                        activityInvokeContext.getActivity().getArguments().addArgs(param_litmus, duration + offsetSecond + "");
                    }
                }
            } else {
                if (sceneFunctionParameterRepository.existParam(sceneFunctionDO.getFunctionId(), param)) {
                    Long duration = experimentTaskDO.getDuration();
                    if (duration != null && duration > 0) {
                        duration = duration > TimeOutMaxValuePreCheckContext.MAX_TIME_OUT ?
                                TimeOutMaxValuePreCheckContext.MAX_TIME_OUT : duration;
                        activityInvokeContext.getActivity().getArguments().addArgs(param, duration + offsetSecond + "");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {

    }
}
