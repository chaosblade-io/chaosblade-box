package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.InvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.InvokeInterceptor;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public class InterceptorInvoker<T extends InvokeInterceptor<Re, Rs>, Re extends InvokeContext,
    Rs> {

    private InvokeInterceptorExecutionChain<T, Re, Rs> invokeInterceptorExecutionChain;

    public InterceptorInvoker(List<T> invokeInterceptors) {
        invokeInterceptorExecutionChain = new InvokeInterceptorExecutionChain<>();
        invokeInterceptorExecutionChain.addInterceptors(invokeInterceptors);
    }

    public Rs invoke(OnceInvoke<Re, Rs> onceInvoke, Re re, Rs defaultResult) {
        Throwable throwable = null;
        try {
            if (invokeInterceptorExecutionChain.applyPreHandle(re, defaultResult)) {
                this.invokeInterceptorExecutionChain.applyPostHandle(onceInvoke, re, defaultResult);
            }
        } catch (Exception exception) {
            throwable = exception;
        }
        this.invokeInterceptorExecutionChain.applyAfterHandle(re, defaultResult, throwable);
        return defaultResult;
    }

}
