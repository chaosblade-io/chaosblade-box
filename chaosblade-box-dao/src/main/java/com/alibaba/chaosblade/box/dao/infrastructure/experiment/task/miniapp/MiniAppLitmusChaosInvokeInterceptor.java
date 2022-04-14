package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.ChaosBladeAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseMiniAppInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * 如果是chaosblade的话需要校验一些逻辑
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class MiniAppLitmusChaosInvokeInterceptor extends BaseMiniAppInvokeInterceptor {

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Override
    public boolean preHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponseReference chaosAppResponseReference) {
        chaosAppResponseReference.setChaosAppResponse(new ChaosBladeAppResponse());
        return preHandle(miniAppInvokeContext, chaosAppResponseReference.getChaosAppResponse());
    }

    @Override
    public boolean preHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse) {
        if (isNotLitmusChaosScene(miniAppInvokeContext)) { return true; }
        ExperimentTaskDO experimentTaskDO = miniAppInvokeContext.getActivityInvokeContext().getContextData()
                .getExperimentTaskDO();
        String experimentTaskId = experimentTaskDO.getTaskId();
        ActivityTaskDO activityTaskDO = miniAppInvokeContext.getActivityInvokeContext().getContextData()
                .getActivityTaskDO();
        return setExpUidWhenRecoveryPhase(miniAppInvokeContext, experimentTaskId, activityTaskDO,chaosAppResponse);
    }

    @Override
    public void afterHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse,
                            Throwable throwable) {
        if (isNotLitmusChaosScene(miniAppInvokeContext)) { return; }
        if (chaosAppResponse instanceof ChaosBladeAppResponse) {
            ChaosBladeAppResponse chaosBladeAppResponse = (ChaosBladeAppResponse)chaosAppResponse;
            PhaseType phaseType = miniAppInvokeContext.getStepExecuteContext().getPhase();
            updateExpRecordWhenRecoveryPhase(miniAppInvokeContext, chaosBladeAppResponse, phaseType);
            createExpRecordWhenAttackOrPreparePhase(miniAppInvokeContext, chaosBladeAppResponse,
                    phaseType);
        }
    }

    private boolean isNotLitmusChaosScene(MiniAppInvokeContext miniAppInvokeContext) {
        String appCode = miniAppInvokeContext.getActivityInvokeContext().getExecutableAppCode();
        return !appCode.startsWith("litmuschaos");
    }

    private boolean setExpUidWhenRecoveryPhase(MiniAppInvokeContext miniAppInvokeContext, String experimentTaskId,
                                               ActivityTaskDO activityTaskDO, ChaosAppResponse chaosAppResponse) {
        if (PhaseType.RECOVER.equals(activityTaskDO.getPhase())) {
            String chaosBladeExpId = getAttackExpId(experimentTaskId,
                    miniAppInvokeContext.getHost(), activityTaskDO);
            if (chaosBladeExpId != null) {
                miniAppInvokeContext.getTempData().add(ChaosBladeAppInvoker.CHAOS_BLADE_EXP_ID, chaosBladeExpId);
            }
        }
        return true;
    }

    private String getAttackExpId(String experimentTaskId, Host host,
                                  ActivityTaskDO activityTaskDO) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = null;
        if (activityTaskDO.getAttackActivityTaskId() != null) {
            chaosBladeExpUidDO = queryExpUidIfFoundAttackActivityTaskId(host, activityTaskDO);
        } else {
            chaosBladeExpUidDO = queryExpUidDOIfNotfoundAttackActivityTaskId(experimentTaskId, host, activityTaskDO);
        }
        return Optional.ofNullable(chaosBladeExpUidDO).map(ChaosBladeExpUidDO::getExpUid).orElse(null);
    }

    private ChaosBladeExpUidDO queryExpUidIfFoundAttackActivityTaskId(Host host, ActivityTaskDO activityTaskDO) {
        ChaosBladeExpUidDO chaosBladeExpUidDO;
        chaosBladeExpUidDO = chaosBladeExpUidRepository.findLastByActivityTaskIdAndHost(
                activityTaskDO.getAttackActivityTaskId(), host.getIp());
        return chaosBladeExpUidDO;
    }

    private ChaosBladeExpUidDO queryExpUidDOIfNotfoundAttackActivityTaskId(String experimentTaskId, Host host,
                                                                           ActivityTaskDO activityTaskDO) {
        ChaosBladeExpUidDO chaosBladeExpUidDO;
        String executableAppCode = activityTaskDO.getExecutableAppCode();
        if (MiniAppUtils.isChaosUninstall(executableAppCode)) {
            String installCode = MiniAppUtils.getInstallCode(activityTaskDO.getExecutableAppCode());
            chaosBladeExpUidDO = chaosBladeExpUidRepository
                    .findLastByExperimentTaskIdAndHostAndAppCodeAndNotExpired(
                            experimentTaskId, host.getIp(), installCode);
        } else {
            chaosBladeExpUidDO = chaosBladeExpUidRepository
                    .findLastByExperimentTaskIdAndHostAndAppCodeAndNotExpired(
                            experimentTaskId, host.getIp(), MiniAppUtils.getAttackCodeByRecoveryCode(executableAppCode));
        }
        return chaosBladeExpUidDO;
    }

    private void createExpRecordWhenAttackOrPreparePhase(MiniAppInvokeContext miniAppInvokeContext,
                                                         ChaosBladeAppResponse chaosBladeAppResponse, PhaseType phaseType) {
        if (PhaseType.PREPARE.equals(phaseType) || PhaseType.ATTACK.equals(phaseType)) {
            String expId = chaosBladeAppResponse.getChaosBladeExpId();
            if (expId != null) {
                createChaosBladeExpRecord(miniAppInvokeContext, expId);
            }
        }
    }

    private void updateExpRecordWhenRecoveryPhase(MiniAppInvokeContext miniAppInvokeContext,
                                                  ChaosBladeAppResponse chaosAppResponse, PhaseType phaseType) {
        if (PhaseType.RECOVER.equals(phaseType)) {
            String attackExpId = miniAppInvokeContext.getTempData().getAsString(
                    ChaosBladeAppInvoker.CHAOS_BLADE_EXP_ID);
            if (attackExpId != null) {
                ChaosBladeExpUidDO chaosBladeExpUidDO;
                ActivityTaskDO activityTaskDO = miniAppInvokeContext.getActivityInvokeContext().getActivityTask();
                if(CommonConstant.APP_CODE_JAVA_AGENT_UNINSTALL.equals(activityTaskDO.getExecutableAppCode())) {
                    chaosBladeExpUidDO = chaosBladeExpUidRepository.findByExpUidAndExperimentTaskId(attackExpId,activityTaskDO.getExperimentTaskId());
                } else {
                    chaosBladeExpUidDO = chaosBladeExpUidRepository.findByExpUid(attackExpId);
                }
                if (chaosAppResponse.isSuccess()) {
                    finishChaosBladeExpRecord(chaosBladeExpUidDO);
                }
            }
        }
    }

    private void createChaosBladeExpRecord(MiniAppInvokeContext miniAppInvokeContext, String expId) {
        String appCode = miniAppInvokeContext.getActivityInvokeContext().getExecutableAppCode();
        String experimentTaskId = miniAppInvokeContext.getActivityInvokeContext().getContextData().getExperimentTaskDO()
                .getTaskId();
        Host host = miniAppInvokeContext.getHost();
        String appExecutionId = miniAppInvokeContext.getMiniAppTaskId();
        String activityTaskId = miniAppInvokeContext.getActivityInvokeContext().getStepExecuteContext().getRequest()
                .getActivityTaskId();
        ActivityTaskDO activityTaskDO = miniAppInvokeContext.getActivityInvokeContext().getContextData()
                .getActivityTaskDO();
        ChaosBladeExpUidDO chaosBladeExpUidDO = buildChaosBladeExpUidDO(expId,
                appCode, experimentTaskId, host, appExecutionId, activityTaskId, activityTaskDO);

        if (chaosBladeExpUidDO.getId() != null) {
            chaosBladeExpUidRepository.update(chaosBladeExpUidDO);
        } else {
            chaosBladeExpUidRepository.add(chaosBladeExpUidDO);
        }
    }

    private ChaosBladeExpUidDO buildChaosBladeExpUidDO(String expId, String appCode, String experimentTaskId, Host host,
                                                       String appExecutionId, String activityTaskId, ActivityTaskDO activityTaskDO) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = getChaosBladeExp(activityTaskId, host);
        chaosBladeExpUidDO.setAppCode(appCode);
        chaosBladeExpUidDO.setExperimentTaskId(experimentTaskId);
        chaosBladeExpUidDO.setHost(host.getIp());
        chaosBladeExpUidDO.setExpUid(expId);
        chaosBladeExpUidDO.setAppExecutionId(appExecutionId);
        chaosBladeExpUidDO.setActivityTaskId(activityTaskId);
        chaosBladeExpUidDO.addAttribute(ChaosBladeExpUidDO.ATTRIBUTE_ACTIVITY_NAME, activityTaskDO.getActivityName());
        return chaosBladeExpUidDO;
    }

    private ChaosBladeExpUidDO getChaosBladeExp(String activityTaskId, Host host) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findLastByActivityTaskIdAndHost(
                activityTaskId, host.getIp());
        if (chaosBladeExpUidDO == null) {
            chaosBladeExpUidDO = new ChaosBladeExpUidDO();
        }
        return chaosBladeExpUidDO;
    }

    private void finishChaosBladeExpRecord(ChaosBladeExpUidDO chaosBladeExpUidDO) {
        chaosBladeExpUidDO.setExpiredTime(new Date());
        chaosBladeExpUidDO.setExpired(true);
        chaosBladeExpUidDO.setGmtModified(new Date());
        chaosBladeExpUidRepository.update(chaosBladeExpUidDO);
    }

    @Override
    public Integer getOrder() {
        return Integer.MIN_VALUE + 1;
    }
}
