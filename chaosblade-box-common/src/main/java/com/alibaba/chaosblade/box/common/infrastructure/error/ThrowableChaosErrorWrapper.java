package com.alibaba.chaosblade.box.common.infrastructure.error;


import com.alibaba.chaosblade.box.common.common.domain.ChaosError;

/**
 * @author haibin
 *
 *
 */
public interface ThrowableChaosErrorWrapper {

    /**
     * 将异常封装成ChaosError
     *
     * @param throwable
     * @return
     */
    public ChaosError wrapper(Throwable throwable);

}
