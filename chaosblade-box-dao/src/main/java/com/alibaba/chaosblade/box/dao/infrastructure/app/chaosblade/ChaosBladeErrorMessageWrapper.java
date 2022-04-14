package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.app.sdk.constants.EnvironmentEnum;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.google.common.base.Strings;

/**
 * @author haibin
 *
 *
 */
public class ChaosBladeErrorMessageWrapper {

    public static String wrapper(EnvironmentEnum environmentEnum, Response response,
                                 ChaosbladeErrorMessageDesc chaosbladeErrorMessageDesc) {
        if (response.isSuccess()) {
            return null;
        }
        int code = response.getCode();
        if (chaosbladeErrorMessageDesc == null) {
            return response.getError();
        }
        CodeMetaData codeMetaData = ChaosBladeResultCodeRepo.getByCode(code).orElse(ConfigFileMiniAppInvokeErrorMessageDescProvider.AGENT_INTERNAL_ERROR);
        String errorSummary = !Strings.isNullOrEmpty(codeMetaData.getCnDesc()) ? codeMetaData.getCnDesc()
                : codeMetaData.getEnDesc();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(errorSummary);
        stringBuilder.append(":");
        stringBuilder.append(chaosbladeErrorMessageDesc.getCnDesc());
        if (chaosbladeErrorMessageDesc.isShowOriginError()) {
            stringBuilder.append("(");
            stringBuilder.append(response.getError());
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }
}
