package com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.sdk.entity.ChaosModels;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.request.LitmusChaosRequest;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;

/**
 * @author yefei
 */
public interface LitmusChaosInvoker {

    /**
     *
     * @return
     * @throws Exception
     */
    ChaosModels getBladeModels() throws Exception;

    /**
     *
     * @param litmusChaosRequest
     * @return
     */
    Response<String> createExp(LitmusChaosRequest litmusChaosRequest);

    /**
     *
     * @param litmusChaosRequest
     * @return
     */
    Response<String> destroyExp(LitmusChaosRequest litmusChaosRequest, String expId);


    Response<String> destroyByChaosBladeExpDO(ChaosBladeExpUidDO chaosBladeExpUidDO);

}
