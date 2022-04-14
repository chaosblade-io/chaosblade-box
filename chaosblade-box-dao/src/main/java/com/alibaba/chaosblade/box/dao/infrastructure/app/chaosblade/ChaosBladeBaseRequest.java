package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ChaosBladeBaseRequest {

    private MiniAppInvokeContext appInvokeContext;

    public ChaosBladeBaseRequest(MiniAppInvokeContext appInvokeContext) {
        this.appInvokeContext = appInvokeContext;
    }

}
