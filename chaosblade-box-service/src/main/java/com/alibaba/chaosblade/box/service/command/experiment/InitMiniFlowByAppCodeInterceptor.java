package com.alibaba.chaosblade.box.service.command.experiment;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface InitMiniFlowByAppCodeInterceptor {

    /**
     * 初始化流程之后
     *
     * @param initMiniFlowByAppCodeInterceptorContext
     */
    public void afterInit(InitMiniFlowByAppCodeInterceptorContext initMiniFlowByAppCodeInterceptorContext);
}
