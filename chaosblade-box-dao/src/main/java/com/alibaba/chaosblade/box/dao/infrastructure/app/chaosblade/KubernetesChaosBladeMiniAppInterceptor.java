package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.PublicCloudUtil;
import com.alibaba.chaosblade.box.common.sdk.entity.K8sResultBean;
import com.alibaba.chaosblade.box.dao.infrastructure.domain.K8sStatusQueryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.invoker.PublicCloudChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.RecordsRepo;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin.lhb
 *
 */
@Component
@Order(1000)
@Slf4j
public class KubernetesChaosBladeMiniAppInterceptor implements ChaosBladeMiniAppInterceptor {

    @Autowired
    private PublicCloudChaosBladeInvoker cloudChaosBladeInvoker;

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Override
    public void preCreate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
                          ChaosBladeAppResponse mkAppResponse) {
    }

    @Override
    public void afterCreate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
        ChaosBladeAppResponse chaosAppResponse) {
        if (isNotK8sScene(miniAppInvokeContext)) {return;}
        String asyncStr = miniAppInvokeContext.getArgs("async");
        boolean isAsync = !Strings.isNullOrEmpty(asyncStr) && Boolean.parseBoolean(asyncStr);
        if (isAsync) {
            RecordsRepo.getMiniAppInvocationRecorder().log("Async invoke under kubernetes");
            return;
        }
        confirmChaosBladeTaskStatus(chaosBladeExpUidDO, miniAppInvokeContext, chaosAppResponse);
    }

    @Override
    public void preUpdate(ChaosBladeExpUidDO chaosBladeExpUidDO, MiniAppInvokeContext miniAppInvokeContext,
        ChaosBladeAppResponse chaosAppResponse) {
        if (isNotK8sScene(miniAppInvokeContext)) {return;}
        confirmChaosBladeTaskStatus(chaosBladeExpUidDO, miniAppInvokeContext, chaosAppResponse);
    }

    /**
     * 确认K8S下面的任务状态
     *
     * @param chaosBladeExpUidDO
     * @param miniAppInvokeContext
     * @param chaosBladeAppResponse
     */
    private void confirmChaosBladeTaskStatus(ChaosBladeExpUidDO chaosBladeExpUidDO,
        MiniAppInvokeContext miniAppInvokeContext,
        ChaosBladeAppResponse chaosBladeAppResponse) {
        PhaseType phaseType = miniAppInvokeContext.getActivityInvokeContext().getStepExecuteContext().getPhase();
        if (Strings.isNullOrEmpty(chaosBladeExpUidDO.getExpUid())) {return;}
        try {
            String expId = chaosBladeExpUidDO.getExpUid();
            RecordsRepo.getMiniAppInvocationRecorder().log("Start Check kubernetes task result by uid:" + expId);
            K8sStatusQueryRequest queryRequest = new K8sStatusQueryRequest(
                miniAppInvokeContext.getHost(), expId,
                PhaseType.ATTACK.equals(phaseType));
            Response<K8sResultBean> k8sResultBeanResponse = cloudChaosBladeInvoker.queryK8sExp(queryRequest);
            K8sResultBean k8sResultBean = k8sResultBeanResponse.getResult();
            chaosBladeAppResponse.setErrorMessage(k8sResultBeanResponse.getError());
            chaosBladeAppResponse.addResponseData("response", k8sResultBeanResponse);
            chaosBladeAppResponse.setChaosBladeResponse(k8sResultBeanResponse);
            //有可能因为超时等原因导致没有结果
            if (k8sResultBean != null) {
                setErrorMessageWhenOneStatus(chaosBladeAppResponse, k8sResultBean);
                chaosBladeAppResponse.setSuccess(k8sResultBean.isSuccess());
                saveSubExpIdIfExit(chaosBladeAppResponse, k8sResultBean);
            }
            RecordsRepo.getMiniAppInvocationRecorder().log("End Check kubernetes task result by uid:" + expId);
        } catch (Exception e) {
            RecordsRepo.getMiniAppInvocationRecorder().log("Check kubernetes task result error", true, e);
        }
    }

    private void saveSubExpIdIfExit(ChaosBladeAppResponse chaosBladeAppResponse,
        K8sResultBean k8sResultBean) {
        try {
            List<K8sResultBean.K8sExpStatusBean> statuses = k8sResultBean.getStatuses();
            if (!CollectionUtil.isNullOrEmpty(statuses)) {
                K8sResultBean.K8sExpStatusBean k8sExpStatusBean = statuses.get(0);
                ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findLastByExpUid(
                    chaosBladeAppResponse.getChaosBladeExpId());
                if (chaosBladeExpUidDO != null) {
                    chaosBladeExpUidDO.setSubExpUid(k8sExpStatusBean.getId());
                    log.info("[save subExpId] expId: {},subId: {},uid: {}",
                        chaosBladeAppResponse.getChaosBladeExpId(), k8sExpStatusBean.getId(),
                        k8sExpStatusBean.getUid());
                    chaosBladeExpUidRepository.update(chaosBladeExpUidDO);
                }
            }
        } catch (Exception e) {
            log.warn("[save subExpId] save subExpId error , expId : {},error: {}",
                chaosBladeAppResponse.getChaosBladeExpId(), e);
        }
    }

    /**
     * 提取内层的结果
     *
     * @param chaosBladeAppResponse
     * @param k8sResultBean
     */
    private void setErrorMessageWhenOneStatus(ChaosBladeAppResponse chaosBladeAppResponse,
        K8sResultBean k8sResultBean) {
        if (!k8sResultBean.isSuccess()) {
            List<K8sResultBean.K8sExpStatusBean> statuses = k8sResultBean.getStatuses();
            if (statuses != null && statuses.size() == 1) {
                chaosBladeAppResponse.setErrorMessage(statuses.get(0).getError());
            }
        }
    }

    private boolean isNotK8sScene(MiniAppInvokeContext miniAppInvokeContext) {
        String appCode = miniAppInvokeContext.getActivityInvokeContext().getAppCode();
        return !PublicCloudUtil.isK8SByAppCode(appCode);
    }

}
