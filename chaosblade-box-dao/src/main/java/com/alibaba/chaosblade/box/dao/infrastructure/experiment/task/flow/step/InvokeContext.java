package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step;


import com.alibaba.chaosblade.box.common.experiment.task.flow.util.HashMapSettings;

import java.util.HashMap;

/**
 * @author haibin
 * 
 * 
 */

public class InvokeContext {

    public StepExecuteContext getStepExecuteContext() {
        return stepExecuteContext;
    }

    public void setStepExecuteContext(StepExecuteContext stepExecuteContext) {
        this.stepExecuteContext = stepExecuteContext;
    }

    private StepExecuteContext stepExecuteContext;

    public HashMapSettings getTempData() {
        return tempData;
    }

    /**
     * 临时性的上下文,用来当做本次活动或者小程序调用时候的上下文用,不会作为整个演练的上下文保存
     */
    private HashMapSettings tempData;

    public InvokeContext() {
        tempData = new HashMapSettings(new HashMap<>());
    }

}

