package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;


import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosbladeErrorMessageDesc;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.CodeMetaData;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;

/**
 * @author haibin
 *
 * 
 */
public interface MiniAppInvokeErrorMessageDescProvider {

    /**
     * @param miniAppInvokeContext
     * @param response
     * @return
     */
    public ChaosbladeErrorMessageDesc getDescByErrorMessage(MiniAppInvokeContext miniAppInvokeContext,
                                                            Response response);

    public CodeMetaData getMetaDataByChaosBladeCode(Integer code);

    public CodeMetaData getMetaDataByErrorCode(String errorCode);

}
