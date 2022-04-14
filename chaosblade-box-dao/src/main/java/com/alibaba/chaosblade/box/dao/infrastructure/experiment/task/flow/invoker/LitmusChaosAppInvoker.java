package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeActionType;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeErrorMessageWrapper;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeResultCodeRepo;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosbladeErrorMessageDesc;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.request.LitmusChaosRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.ChaosBladeAppInvoker.CHAOS_BLADE_EXP_ID;

/**
 * @author yefei
 */
@Slf4j
public class LitmusChaosAppInvoker implements MiniAppInvoker {

    @Override
    public boolean support(MiniAppInvokeContext miniAppInvokeContext) {
        String appCode = miniAppInvokeContext.getActivityInvokeContext().getExecutableAppCode();
        return appCode != null && appCode.startsWith("litmuschaos");
    }

    @Override
    public ChaosAppResponseReference invoke(MiniAppInvokeContext miniAppInvokeContext) {
        ChaosAppResponseReference chaosAppResponseReference = new ChaosAppResponseReference();

        LitmusChaosInvoker litmusChaosInvoker = miniAppInvokeContext.getStepExecuteContext().getFlowEngineContext()
                .getFlowEngineConfig().getLitmusChaosInvoker();

        String appCode = miniAppInvokeContext.getActivityInvokeContext().getExecutableAppCode();
        ChaosBladeAction chaosBladeAction = ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode);

        ChaosBladeAppResponse chaosBladeAppResponse;
        String expId = miniAppInvokeContext.getTempData().getAsString(CHAOS_BLADE_EXP_ID);

        switch (chaosBladeAction.getActionType()) {
            case ATTACK:
                try {
                    Response<String> response = litmusChaosInvoker.createExp(new LitmusChaosRequest(miniAppInvokeContext, chaosBladeAction));
                    chaosBladeAppResponse = fromResponse(miniAppInvokeContext, response);
                } catch (Exception exception) {
                    log.info("[LitmusChaosAppInvoker] error message:", exception);
                    //用父类后面会报错 class cast
                    chaosBladeAppResponse = new ChaosBladeAppResponse();
                    chaosBladeAppResponse.setSuccess(false);
                    chaosBladeAppResponse.setScope(miniAppInvokeContext.getHost());
                    chaosBladeAppResponse.setErrorMessage(exception.getMessage());
                }
                chaosAppResponseReference.setChaosAppResponse(chaosBladeAppResponse);
                return chaosAppResponseReference;
            case STOP_ATTACK:
                if (Strings.isNullOrEmpty(expId) && actionNeedExpId(chaosBladeAction.getActionType())) {
                    chaosBladeAppResponse = new ChaosBladeAppResponse();
                    chaosBladeAppResponse.setSuccess(true);
                    Map<String, Object> data = new HashMap<>();
                    data.put("说明", "无前置对应操作或者对应操作失败,无需恢复");
                    chaosBladeAppResponse.setData(data);
                    chaosAppResponseReference.setChaosAppResponse(chaosBladeAppResponse);
                    return chaosAppResponseReference;
                }
                try {
                    Response<String> response = litmusChaosInvoker.destroyExp(new LitmusChaosRequest(miniAppInvokeContext, chaosBladeAction), expId);
                    chaosBladeAppResponse = fromResponse(miniAppInvokeContext, response);
                } catch (Exception exception) {
                    log.info("[LitmusChaosAppInvoker] error message:", exception);
                    //用父类后面会报错 class cast
                    chaosBladeAppResponse = new ChaosBladeAppResponse();
                    chaosBladeAppResponse.setSuccess(false);
                    chaosBladeAppResponse.setScope(miniAppInvokeContext.getHost());
                    chaosBladeAppResponse.setErrorMessage(exception.getMessage());
                }
                chaosAppResponseReference.setChaosAppResponse(chaosBladeAppResponse);
                return chaosAppResponseReference;
            default:
                return null;
        }
    }

    private boolean actionNeedExpId(ChaosBladeActionType chaosBladeActionType) {
        return ChaosBladeActionType.STOP_ATTACK.equals(chaosBladeActionType) || ChaosBladeActionType.UNINSTALL_AGENT
                .equals(chaosBladeActionType);
    }

    private ChaosBladeAppResponse fromResponse(MiniAppInvokeContext appInvokeContext, Response response) {
        if (response == null) {
            return new ChaosBladeAppResponse();
        }
        ChaosBladeAppResponse chaosAppResponse = new ChaosBladeAppResponse();
        chaosAppResponse.setSuccess(response.isSuccess());
        if (response.getResult() != null && response.isSuccess() && !PhaseType.RECOVER.equals(
                appInvokeContext.getStepExecuteContext().getPhase())) {
            if (response.getResult() instanceof String) {
                chaosAppResponse.setChaosBladeExpId(response.getResult().toString());
            }
        }
        ChaosbladeErrorMessageDesc chaosbladeErrorMessageDesc = ChaosBladeResultCodeRepo.getDescByMessage(
                appInvokeContext,
                response).orElse(null);
        if (chaosbladeErrorMessageDesc != null) {
            chaosAppResponse.setSuccess(!chaosbladeErrorMessageDesc.getShouldFail());
            chaosAppResponse.setStatus(chaosbladeErrorMessageDesc.getStatus());
        }
        chaosAppResponse.setChaosBladeResponse(response);
        chaosAppResponse.setErrorMessage(ChaosBladeErrorMessageWrapper.wrapper(
                appInvokeContext.getActivityInvokeContext().getStepExecuteContext().getFlowEngineContext().getEnvironment(),
                response, chaosbladeErrorMessageDesc));
        return chaosAppResponse;
    }
}
