package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeActionType;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeDestroyExpRequest;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeExpQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeRevokeExpRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.ChaosBladeStatus;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.common.sdk.entity.PrepareStatusBean;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.*;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haibin
 *
 * 
 */
@Slf4j
public class ChaosBladeAppInvoker implements MiniAppInvoker {

    public static String CHAOS_BLADE_EXP_ID = "chaos_exp_id";

    public static String PREPARE_CHAOS_BLADE_EXP_ID = "prepare_chaos_exp_id";

    public static String CHAOSBLADE_METRIC_OPERATION_RECOVERY = "recovery";

    public static String CHAOSBLADE_METRIC_OPERATION_INJECTION = "injection";

    public static String CHAOSBLADE_METRIC_OPERATION_PREPARE = "prepare";

    @Override
    public boolean support(MiniAppInvokeContext appInvokeContext) {
        String appCode = appInvokeContext.getActivityInvokeContext().getExecutableAppCode();
        return appCode != null && appCode.startsWith("chaos");
    }

    @Override
    public ChaosAppResponseReference invoke(MiniAppInvokeContext appInvokeContext) {
        ChaosAppResponseReference chaosAppResponseReference = new ChaosAppResponseReference();
        String expId = appInvokeContext.getTempData().getAsString(CHAOS_BLADE_EXP_ID);
        String appCode = appInvokeContext.getActivityInvokeContext().getExecutableAppCode();
        ChaosBladeAction chaosBladeAction = ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode);
        ChaosBladeAppResponse chaosBladeAppResponse = new ChaosBladeAppResponse();
        if (chaosBladeAction == null) {
            chaosBladeAppResponse.setErrorMessage(
                    "appCode not found:" + appCode);
            chaosAppResponseReference.setChaosAppResponse(chaosBladeAppResponse);
            return chaosAppResponseReference;
        }
        if (Strings.isNullOrEmpty(expId) && actionNeedExpId(chaosBladeAction.getActionType())) {
            chaosBladeAppResponse.setSuccess(true);
            Map<String, Object> data = new HashMap<>();
            data.put("说明", "无前置对应操作或者对应操作失败,无需恢复");
            chaosBladeAppResponse.setData(data);
            chaosAppResponseReference.setChaosAppResponse(chaosBladeAppResponse);
            return chaosAppResponseReference;
        }
        ChaosBladeInvoker chaosBladeInvoker = appInvokeContext.getStepExecuteContext().getFlowEngineContext()
                .getFlowEngineConfig().getChaosBladeInvoker();
        ChaosAppResponse chaosAppResponse = null;
        Response response = null;
        try {
            response = invoke(chaosBladeInvoker, appInvokeContext, chaosBladeAction, expId);
            log.info("[ChaosBladeAppInvoker] ChaosBladeAppInvoker response:{}",response.getResult());
            chaosAppResponse = fromResponse(appInvokeContext, response);
        } catch (Exception exception) {
            log.info("[ChaosBladeAppInvoker] error message:",exception);
            //用父类后面会报错 class cast
            chaosAppResponse = new ChaosBladeAppResponse();
            chaosAppResponse.setSuccess(false);
            chaosAppResponse.setScope(appInvokeContext.getHost());
            chaosAppResponse.setErrorMessage(exception.getMessage());
        }
        chaosAppResponseReference.setChaosAppResponse(chaosAppResponse);
        return chaosAppResponseReference;
    }

    private Response invoke(ChaosBladeInvoker chaosBladeInvoker, MiniAppInvokeContext appInvokeContext,
                            ChaosBladeAction chaosBladeAction, String expId) {
        Response response = null;
        switch (chaosBladeAction.getActionType()) {
            case ATTACK:
                response = invokeChaosBlade(new Supplier<Response>() {
                    @Override
                    public Response get() {
                        ChaosBladeCreateExpRequest chaosBladeCreateExpRequest = new ChaosBladeCreateExpRequest(
                                appInvokeContext,
                                chaosBladeAction);
                        return chaosBladeInvoker.createExp(chaosBladeCreateExpRequest);
                    }
                }, CHAOSBLADE_METRIC_OPERATION_INJECTION);
                break;
            case STOP_ATTACK:
                response = invokeChaosBlade(new Supplier<Response>() {
                    @Override
                    public Response get() {
                        ChaosBladeDestroyExpRequest chaosBladeDestroyExpRequest = new ChaosBladeDestroyExpRequest(
                                appInvokeContext.getHost(), expId,
                                appInvokeContext.getActivityInvokeContext().getExecutableAppCode());
                        return chaosBladeInvoker.destroyExp(chaosBladeDestroyExpRequest);
                    }
                }, CHAOSBLADE_METRIC_OPERATION_RECOVERY);
                checkJavaAgentStatus(response, chaosBladeInvoker, appInvokeContext);
                break;
            case INSTALL_AGENT:
                response = invokeChaosBlade(new Supplier<Response>() {
                    @Override
                    public Response get() {
                        return installAgent(expId, new Supplier<Response<String>>() {
                            @Override
                            public Response<String> get() {
                                ChaosBladePrepareExpRequest chaosBladePrepareExpRequest
                                        = new ChaosBladePrepareExpRequest(
                                        appInvokeContext, chaosBladeAction);
                                return chaosBladeInvoker.prepareExp(chaosBladePrepareExpRequest);
                            }
                        });
                    }
                }, CHAOSBLADE_METRIC_OPERATION_PREPARE);
                break;
            case UNINSTALL_AGENT:
                response = invokeChaosBlade(new Supplier<Response>() {
                    @Override
                    public Response get() {
                        ChaosBladeRevokeExpRequest chaosBladeRevokeExpRequest = new ChaosBladeRevokeExpRequest(
                                appInvokeContext.getHost(), expId);
                        return chaosBladeInvoker.revokeExp(chaosBladeRevokeExpRequest);
                    }
                }, CHAOSBLADE_METRIC_OPERATION_RECOVERY);
                break;
            default:
                response = null;
        }
        return response;
    }

    private Response invokeChaosBlade(Supplier<Response> supplier, String name) {
        StopWatch stopWatch = StopWatch.createStarted();
        Response response = null;
        try {
            response = supplier.get();
        } finally {
            stopWatch.stop();
//            if (response != null && response.isSuccess()) {
//                ChaosApplicationMetric.getChaosBladeCompass(name).record(stopWatch.getTime(TimeUnit.MILLISECONDS),
//                        ChaosApplicationMetric.SUCCESS);
//            } else {
//                ChaosApplicationMetric.getChaosBladeCompass(name).record(stopWatch.getTime(TimeUnit.MILLISECONDS),
//                        ChaosApplicationMetric.ERROR);
//            }
        }
        return response;
    }

    /**
     * 安装agent
     * 1.根据前一个expId查询是否正在运行，如果已经安装了就不需要再次安装
     * 2.如果没有expId,那么久尝试安装，如果安装失败，说明有mk1的client，然后再卸载后安装
     */
    private Response installAgent(String expId, Supplier<Response<String>> prepareFunction) {
        if (Strings.isNullOrEmpty(expId)) {
            //安装
            Response<String> response = prepareFunction.get();
            if (response.isSuccess()) {
                return response;
            } else {
                if (response.getCode() == ChaosBladeMetaData.AGENT_DUMPLICATE_CODE) {
                    //强制终止agent
                }
            }
            return response;
        }
        return prepareFunction.get();
    }

    private boolean actionNeedExpId(ChaosBladeActionType chaosBladeActionType) {
        return ChaosBladeActionType.STOP_ATTACK.equals(chaosBladeActionType) || ChaosBladeActionType.UNINSTALL_AGENT
                .equals(chaosBladeActionType);
    }

    private ChaosBladeAppResponse fromResponse(MiniAppInvokeContext appInvokeContext, Response response) {
        if (response == null) { return new ChaosBladeAppResponse(); }
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
        chaosAppResponse.setOriginErrorMessage(response.getError());
        chaosAppResponse.setChaosBladeResponse(response);
        chaosAppResponse.setErrorMessage(ChaosBladeErrorMessageWrapper.wrapper(
                appInvokeContext.getActivityInvokeContext().getStepExecuteContext().getFlowEngineContext().getEnvironment(),
                response, chaosbladeErrorMessageDesc));
        return chaosAppResponse;
    }

    private void checkJavaAgentStatus(Response response, ChaosBladeInvoker chaosBladeInvoker,
                                      MiniAppInvokeContext appInvokeContext) {
        try {
            if (response != null && !response.isSuccess() &&
                    !MiniAppUtils.isK8S(appInvokeContext.getActivityInvokeContext().getExecutableAppCode()) &&
                    MiniAppUtils.isJvm(appInvokeContext.getActivityInvokeContext().getExecutableAppCode())) {
                String expId = appInvokeContext.getTempData().getAsString(PREPARE_CHAOS_BLADE_EXP_ID);
                Response<PrepareStatusBean> prepareStatusBeanResponse = chaosBladeInvoker.getPrepareStatus(
                        new ChaosBladeExpQueryRequest(appInvokeContext.getHost(), expId));
                PrepareStatusBean prepareStatusBean = prepareStatusBeanResponse.getResult();
                if (ChaosBladeStatus.Revoked.name().equals(prepareStatusBean.getStatus())) {
                    log.info("[ChaosBladeAppInvoker] change response, expId: {},error:{}", expId, response.getError());
                    //若java agent已经卸载，则jvm演练已经被销毁，jvm的恢复阶段要在页面上显示恢复成功
                    response.setSuccess(true);
                    response.setError(null);
                }
            }
        } catch (Exception e) {
            //异常吞掉，不要影响主要流程
            log.warn("[ChaosBladeAppInvoker] checkJavaAgentStatus fail",e);
        }
    }

}
