package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;

/**
 * @author haibin
 *
 *
 */
public interface MiniAppInvoker extends OnceInvoke<MiniAppInvokeContext, ChaosAppResponseReference> {

    /**
     * 是否支持当前小程序的调用
     *
     * @param miniAppInvokeContext 小程序调用上下文
     * @return
     */
    public boolean support(MiniAppInvokeContext miniAppInvokeContext);

    /**
     * 调用小程序
     *
     * @param miniAppInvokeContext 小程序调用上下文
     * @return
     */
    @Override
    public ChaosAppResponseReference invoke(MiniAppInvokeContext miniAppInvokeContext);

}
