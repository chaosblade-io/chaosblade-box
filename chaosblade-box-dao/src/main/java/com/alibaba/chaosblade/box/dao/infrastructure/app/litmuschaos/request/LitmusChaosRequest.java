package com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.request;

import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import lombok.Getter;

/**
 * @author yefei
 */
@Getter
public class LitmusChaosRequest {

    private MiniAppInvokeContext appInvokeContext;

    private ChaosBladeAction chaosBladeAction;

    public LitmusChaosRequest(MiniAppInvokeContext appInvokeContext, ChaosBladeAction chaosBladeAction) {
        this.appInvokeContext = appInvokeContext;
        this.chaosBladeAction = chaosBladeAction;
    }
}
