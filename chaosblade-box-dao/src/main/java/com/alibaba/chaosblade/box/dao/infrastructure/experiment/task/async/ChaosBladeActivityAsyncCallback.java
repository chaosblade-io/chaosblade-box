package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeExpQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.ChaosBladeStatus;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.ChaosTools;
import com.alibaba.chaosblade.box.common.infrastructure.util.PublicCloudUtil;
import com.alibaba.chaosblade.box.common.sdk.entity.ExpStatusBean;
import com.alibaba.chaosblade.box.common.sdk.entity.K8sResultBean;
import com.alibaba.chaosblade.box.common.sdk.util.CliUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskPusher;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.RecordsRepo;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Component
public class ChaosBladeActivityAsyncCallback implements ActivityAsyncCallback {

    @Autowired
    private ChaosBladeInvoker chaosBladeInvoker;

    private static Integer MAX_INSTALL_TIME_MINUTES = 3;

    @Autowired
    private ExperimentTaskPusher experimentTaskPusher;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private AsyncCallBackHostInterceptor asyncCallBackHostInterceptor;
    public ChaosBladeActivityAsyncCallback() {
    }

    @Override
    public void execute(AsyncCallBackContext asyncCallBackContext) {
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = asyncCallBackContext.getExperimentMiniAppTaskDO();
        ChaosBladeExpUidDO chaosBladeExpUidDO = asyncCallBackContext.getChaosBladeExpUidDO();
        Host host = initHost(experimentMiniAppTaskDO);
        if (Strings.isNullOrEmpty(chaosBladeExpUidDO.getExpUid())) {
            RecordsRepo.getMiniAppInvocationRecorder().log(asyncCallBackContext.getExperimentMiniAppTaskDO(), "Current expId missed,try later",true);
            return;
        }
        handleInHostMode(experimentMiniAppTaskDO, chaosBladeExpUidDO, host);
        handleInKubernetesMode(experimentMiniAppTaskDO, chaosBladeExpUidDO, host, asyncCallBackContext);
    }

    private void handleInKubernetesMode(ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                                        ChaosBladeExpUidDO chaosBladeExpUidDO,
                                        Host host, AsyncCallBackContext asyncCallBackContext) {
        if (isK8sScene(experimentMiniAppTaskDO.getAppCode())) {
            if ("Running".equals(asyncCallBackContext.getStatus())) {
                //如果客户端已经显示成功了,那就不用在查询了
                markRunSuccess(experimentMiniAppTaskDO);
                K8sResultBean k8sResultBean = new K8sResultBean(chaosBladeExpUidDO.getExpUid(), true, null,
                        new ArrayList<>());
                pushTasks(experimentMiniAppTaskDO, k8sResultBean);
            } else {
                Response<K8sResultBean> k8sResultBeanResponse = null;
                try {
                    k8sResultBeanResponse = chaosBladeInvoker.queryK8sExp(host,
                            chaosBladeExpUidDO.getExpUid(), PhaseType.ATTACK.equals(
                                    experimentMiniAppTaskDO.getPhase()));
                    K8sResultBean k8sResultBean = k8sResultBeanResponse.getResult();
                    handleInjectionTimeoutInKubernetesMode(experimentMiniAppTaskDO, k8sResultBean);
                    handleInjectionFinishedInKubernetesMode(experimentMiniAppTaskDO, k8sResultBean);
                    pushTasks(experimentMiniAppTaskDO, k8sResultBean);
                } catch (Exception e) {

                }
            }

        }
    }

    private void pushTasks(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, Object resultBean) {
        if (resultBean != null) {
            experimentMiniAppTaskDO.setData(JSON.toJSONString(resultBean));
        }
        if (experimentMiniAppTaskDO.isFinished()) {
            boolean success = activityTargetExecutionResultRepository.updateNotFinishedTask(experimentMiniAppTaskDO);
            if (success) {
                experimentTaskPusher.pushByAsyncWay(experimentMiniAppTaskDO);
            }
        }
    }

    private void handleInjectionFinishedInKubernetesMode(ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                                                         K8sResultBean k8sResultBean) {
        if (isCompleted(experimentMiniAppTaskDO, k8sResultBean)) {
            if (k8sResultBean.isSuccess()) {
                markRunSuccess(experimentMiniAppTaskDO);
            } else {
                markRunFailed(experimentMiniAppTaskDO, null, k8sResultBean.getError());
            }
        }
    }

    private void handleInjectionTimeoutInKubernetesMode(ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                                                        K8sResultBean k8sResultBean) {
        if (!isCompleted(experimentMiniAppTaskDO, k8sResultBean)) {
            if (runTimeOut(experimentMiniAppTaskDO)) {
                markRunFailed(experimentMiniAppTaskDO, CommonErrorCode.B_CHAOS_BLADE_ASYNC_TIMEOUT.name(),
                        CommonErrorCode.B_CHAOS_BLADE_ASYNC_TIMEOUT.getReadableMessage() + ",超时时间:"
                                + MAX_INSTALL_TIME_MINUTES + " 分钟");
            }
        }
    }

    private boolean isCompleted(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, K8sResultBean k8sResultBean) {
        return k8sResultBean.completed(
                PhaseType.ATTACK.equals(experimentMiniAppTaskDO.getPhase()) ? CliUtil.CREATE : CliUtil.DESTROY);
    }

    private void handleInHostMode(ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                                  ChaosBladeExpUidDO chaosBladeExpUidDO,
                                  Host host) {
        ExpStatusBean expStatusBean;
        if (!isK8sScene(experimentMiniAppTaskDO.getAppCode())) {
            Response<ExpStatusBean> expStatus = null;
            try {
                expStatus = chaosBladeInvoker.getExpStatus(
                        new ChaosBladeExpQueryRequest(host, chaosBladeExpUidDO.getExpUid()));
                expStatusBean = expStatus.getResult();
                handleInjectionTimeOutInHostMode(experimentMiniAppTaskDO, expStatusBean);
                handleInjectionFinishedInHostMode(experimentMiniAppTaskDO, expStatusBean);
                pushTasks(experimentMiniAppTaskDO, expStatusBean);
            } catch (Exception e) {
                log.warn("handle host callback failed", e);
            }

        }
    }

    private void handleInjectionTimeOutInHostMode(ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                                                  ExpStatusBean expStatusBean) {
        if (ChaosBladeStatus.Created.name().equals(expStatusBean.getStatus())) {
            if (runTimeOut(experimentMiniAppTaskDO)) {
                markRunFailed(experimentMiniAppTaskDO, CommonErrorCode.B_CHAOS_BLADE_ASYNC_TIMEOUT.name(),
                        CommonErrorCode.B_CHAOS_BLADE_ASYNC_TIMEOUT.getReadableMessage() + ",超时时间:"
                                + MAX_INSTALL_TIME_MINUTES + " 分钟");
            }
        }
    }

    private void handleInjectionFinishedInHostMode(ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                                                   ExpStatusBean expStatusBean) {
        if (ChaosBladeStatus.Revoked.name().equals(expStatusBean.getStatus())) {
            markRunFailed(experimentMiniAppTaskDO, CommonErrorCode.B_CHAOS_BLADE_ASYNC_REVOKED.name(),
                    CommonErrorCode.B_CHAOS_BLADE_ASYNC_REVOKED.getReadableMessage());
        }
        if (ChaosBladeStatus.Error.name().equals(expStatusBean.getStatus())) {
            markRunFailed(experimentMiniAppTaskDO, CommonErrorCode.B_CHAOS_BLADE_ASYNC_ERROR.name(),
                    expStatusBean.getError());
        }
        if (!Strings.isNullOrEmpty(expStatusBean.getError())) {
            markRunFailed(experimentMiniAppTaskDO, CommonErrorCode.B_CHAOS_BLADE_ASYNC_ERROR.name(),
                    expStatusBean.getError());
        }
        if (expStatusBean.isRunning()) {
            log.info("[ChaosBladeActivityAsyncCallback] expStatusBean.isRunning() : {}",
                    expStatusBean.isRunning());
            markRunSuccess(experimentMiniAppTaskDO);
        }
    }

    private Host initHost(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        Host host = new Host();
        host.setIp(experimentMiniAppTaskDO.getHostIp());
        host.setDeviceConfigurationId(experimentMiniAppTaskDO.getDeviceConfigurationId());
        host.setTargetIp(experimentMiniAppTaskDO.getHostIp());
        if (PublicCloudUtil.isK8SByAppCode(experimentMiniAppTaskDO.getAppCode())) {
            asyncCallBackHostInterceptor.fillHostInfo(host);
        }
        return host;
    }

    @Override
    public void expired(AsyncCallBackContext asyncCallBackContext) {
        chaosBladeInvoker.destroyByChaosBladeExpDO(asyncCallBackContext.getChaosBladeExpUidDO());
    }

    @Override
    public boolean support(String toolType) {
        return ChaosTools.CHAOS_BLADE.name().equals(toolType);
    }

    @Override
    public boolean supportByAppCode(String appCode) {
        return MiniAppUtils.isFromChaosBlade(appCode);
    }

    private boolean runTimeOut(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        boolean isTimeout = System.currentTimeMillis() - experimentMiniAppTaskDO.getStartTime().getTime()
                > MAX_INSTALL_TIME_MINUTES * 60 * 1000;
        if (isTimeout) {
            RecordsRepo.getMiniAppInvocationRecorder().log(
                    "Execute timeout after:" + MAX_INSTALL_TIME_MINUTES + " minutes");
        }
        return isTimeout;
    }

    public void markRunFailed(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, String errorCode,
                              String errorMessage) {
        experimentMiniAppTaskDO.setState(StateEnum.FINISHED.getValue());
        experimentMiniAppTaskDO.setResult(ResultEnum.FAILED.getValue());
        experimentMiniAppTaskDO.setErrorCode(errorCode);
        experimentMiniAppTaskDO.setErrorMessage(errorMessage);
        experimentMiniAppTaskDO.setGmtEnd(new Date());
    }

    public void markRunSuccess(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        experimentMiniAppTaskDO.setState(StateEnum.FINISHED.getValue());
        experimentMiniAppTaskDO.setResult(ResultEnum.SUCCESS.getValue());
        experimentMiniAppTaskDO.setGmtEnd(new Date());
    }

    private boolean isK8sScene(String appCode) {
        return PublicCloudUtil.isK8SByAppCode(appCode);
    }

}
