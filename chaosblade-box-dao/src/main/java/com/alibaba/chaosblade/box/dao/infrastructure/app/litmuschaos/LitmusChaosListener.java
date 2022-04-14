package com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos;


import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;

/**
 * @author yefei
 */
public interface LitmusChaosListener {

    /**
     *
     * @param litmusChaosInvoker
     * @param cloud
     * @return
     * @throws Exception
     */
    ChaosBladeMetaData onCompleted(LitmusChaosInvoker litmusChaosInvoker) throws Exception;
}
