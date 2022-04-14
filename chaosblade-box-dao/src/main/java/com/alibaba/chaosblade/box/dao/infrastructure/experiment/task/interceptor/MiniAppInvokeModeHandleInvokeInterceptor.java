package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;


import com.alibaba.chaosblade.box.common.app.sdk.InvokeMode;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosFunctionDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.util.ChaosAppUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用来统计一个演练任务下面所有每台机器上面每种故障注入场景影响的请求数目
 *
 * @author haibin
 *
 *
 */
@Component
public class MiniAppInvokeModeHandleInvokeInterceptor extends BaseActivityInvokeInterceptor {

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        activityInvokeContext.getStepExecuteContext().getChaosAppContext().addData("experiment_task_id",
            activityInvokeContext.getContextData().getExperimentTaskId());
        if (!MiniAppUtils.isFromChaosBlade(activityInvokeContext.getExecutableAppCode())) {
            ChaosFunctionDescriptor chaosFunctionDescriptor = (ChaosFunctionDescriptor) ChaosAppUtil.getChaosMethodDescriptor(
                activityInvokeContext.getExecutableAppCode());
            if (chaosFunctionDescriptor != null) {
                InvokeMode invokeMode = chaosFunctionDescriptor.getInvokeMode();
                if (InvokeMode.ONCE.equals(invokeMode)) {
                    setOneScope(activityInvokeContext);
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {
    }

    /**
     * 使用自定义小查询去查询请求数目，在小程序的实现里面已经包含了对每台机器的查询,不需要在这里对每一台机器做轮询,所以需要把scope设置为空
     *
     * @param activityInvokeContext
     */
    private void setOneScope(ActivityInvokeContext activityInvokeContext) {
        List<Host> scops = activityInvokeContext.getActivity().getScope();
        if (!CollectionUtil.isNullOrEmpty(scops)) {
            Host firstScope = scops.get(0);
            activityInvokeContext.getActivity().getDefinition().setScope(Lists.newArrayList(firstScope));
        }
    }
}
