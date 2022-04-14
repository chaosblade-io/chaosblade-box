package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;

/**
 * @author haibin
 *
 *
 */
public interface ChaosBladeMiniAppInterceptor {

    /**
     * 创建chaosblade调用记录之前的拦截处理
     *
     * @param chaosBladeExpUidDO
     * @param miniAppInvokeContext
     */
    public void preCreate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
                          ChaosBladeAppResponse chaosAppResponse);

    /**
     * 创建之后
     *
     * @param chaosBladeExpUidDO
     * @param miniAppInvokeContext
     * @param chaosAppResponse
     */
    public void afterCreate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
        ChaosBladeAppResponse chaosAppResponse);

    /**
     * 灰度节点更新chaosblade记录之前的拦截处理
     *
     * @param chaosBladeExpUidDO
     * @param miniAppInvokeContext
     */
    public void preUpdate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
        ChaosBladeAppResponse chaosAppResponse);
}
