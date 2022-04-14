package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ChaosBladeCreateExpRequest extends ChaosBladeBaseRequest {

    private ChaosBladeAction chaosBladeAction;

    public ChaosBladeCreateExpRequest(MiniAppInvokeContext appInvokeContext, ChaosBladeAction chaosBladeAction) {
        super(appInvokeContext);
        this.chaosBladeAction = chaosBladeAction;

    }
}
