package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.MiniAppInvokeErrorMessageDescProvider;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.google.common.base.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Component
public class ChaosBladeResultCodeRepo implements InitializingBean {

    @Autowired
    private List<MiniAppInvokeErrorMessageDescProvider> miniAppInvokeErrorMessageDescProviders;

    protected static List<MiniAppInvokeErrorMessageDescProvider> globalProviders;

    public static Optional<ChaosbladeErrorMessageDesc> getDescByMessage(MiniAppInvokeContext miniAppInvokeContext,
                                                                        Response response) {
        if (Strings.isNullOrEmpty(response.getError())) {
            return Optional.empty();
        }
        ChaosbladeErrorMessageDesc chaosbladeErrorMessageDesc = null;
        for (MiniAppInvokeErrorMessageDescProvider miniAppInvokeErrorMessageDescProvider :
                globalProviders) {
            chaosbladeErrorMessageDesc = miniAppInvokeErrorMessageDescProvider.getDescByErrorMessage(
                    miniAppInvokeContext,
                    response);
            if (chaosbladeErrorMessageDesc != null) {
                break;
            }
        }
        return Optional.ofNullable(chaosbladeErrorMessageDesc);
    }

    public static Optional<CodeMetaData> getByCode(Integer code) {
        if (code == null) {
            return Optional.empty();
        }
        CodeMetaData codeMetaData = null;
        for (MiniAppInvokeErrorMessageDescProvider miniAppInvokeErrorMessageDescProvider :
                globalProviders) {
            codeMetaData = miniAppInvokeErrorMessageDescProvider.getMetaDataByChaosBladeCode(code);
            if (codeMetaData != null) {
                break;
            }
        }
        return Optional.ofNullable(codeMetaData);
    }

    public static Optional<CodeMetaData> getByErrorCode(String errorCode) {
        if (errorCode == null) {
            return Optional.empty();
        }
        CodeMetaData codeMetaData = null;
        for (MiniAppInvokeErrorMessageDescProvider miniAppInvokeErrorMessageDescProvider :
                globalProviders) {
            codeMetaData = miniAppInvokeErrorMessageDescProvider.getMetaDataByErrorCode(errorCode);
            if (codeMetaData != null) {
                break;
            }
        }
        return Optional.ofNullable(codeMetaData);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        globalProviders = miniAppInvokeErrorMessageDescProviders;
    }

}
