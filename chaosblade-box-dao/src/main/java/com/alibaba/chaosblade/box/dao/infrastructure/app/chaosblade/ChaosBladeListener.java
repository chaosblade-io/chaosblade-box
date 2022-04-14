package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;

/**
 * @author haibin
 *
 *
 */
public interface ChaosBladeListener {

    /**
     * 当claos blade加载完毕之后
     *
     * @param chaosBladeInvoker
     * @param cloud
     * @return
     * @throws Exception
     */
    public ChaosBladeMetaData onCompleted(ChaosBladeInvoker chaosBladeInvoker) throws Exception;

}
