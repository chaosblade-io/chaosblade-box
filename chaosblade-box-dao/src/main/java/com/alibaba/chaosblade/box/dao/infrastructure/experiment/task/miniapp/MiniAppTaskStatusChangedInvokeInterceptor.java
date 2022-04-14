package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.LogFormats;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppInvokeStatusCode;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskAsyncSupport;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseMiniAppInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.RecordsRepo;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用来更改小程序任务状态
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class MiniAppTaskStatusChangedInvokeInterceptor extends BaseMiniAppInvokeInterceptor {

    @Autowired
    private MiniAppTaskManager miniAppTaskManager;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private ActivityTaskAsyncSupport activityTaskAsyncSupport;

    @Override
    public boolean preHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse) {
        RecordsRepo.getMiniAppInvocationRecorder().log("Start invoke");
        String activityTaskId = miniAppInvokeContext.getActivityInvokeContext().getStepExecuteContext()
                .getActivityTaskId();
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = miniAppTaskManager.findOrCreateMiniAppTaskIfNotExist(
                miniAppInvokeContext.getActivityInvokeContext().getActivityTask(),
                miniAppInvokeContext.getHost());
        miniAppInvokeContext.setMiniAppTaskDO(experimentMiniAppTaskDO);
        miniAppInvokeContext.setMiniAppTaskId(experimentMiniAppTaskDO.getTaskId());
        MiniAppInvokeFlowThreadLocalContext.startMiniAppInvoke(experimentMiniAppTaskDO, false);
        boolean success = activityTargetExecutionResultRepository.startTask(experimentMiniAppTaskDO.getTaskId(),
                activityTaskId, StateEnum.RUNNING);
        if (!success) {
            chaosAppResponse.setSuccess(false);
            chaosAppResponse.setErrorMessage(CommonErrorCode.B_ACTIVITY_ALREADY_RUNNING.name());
            return false;
        }
        return true;
    }

    @Override
    public void afterHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse,
                            Throwable throwable) {
        String appExecutionId = miniAppInvokeContext.getMiniAppTaskId();
        if (appExecutionId == null) {
            log.warn("AppExecutionId lost");
            return;
        }
        try {
            String activityTaskId = miniAppInvokeContext.getStepExecuteContext().getRequest().getActivityTaskId();
            ActivityTaskDO activityTaskDO = miniAppInvokeContext.getActivityInvokeContext().getActivityTask();
            if (!isAsyncRunning(chaosAppResponse, activityTaskDO)) {
                saveTask(appExecutionId, throwable, chaosAppResponse, miniAppInvokeContext,
                        activityTaskId);
            } else {
                miniAppInvokeContext.getActivityInvokeContext().setAsync(true);
                RecordsRepo.getMiniAppInvocationRecorder().log("Async Invoke,Not Change task status");
            }
        } catch (Exception exception) {
            RecordsRepo.getMiniAppInvocationRecorder().log("Update result internal error", false, exception);
        } finally {
            RecordsRepo.getMiniAppInvocationRecorder().log("End invoke");
            MiniAppInvokeFlowThreadLocalContext.endMiniAppInvoke();
        }
    }

    private boolean isAsyncRunning(ChaosAppResponse chaosAppResponse, ActivityTaskDO activityTaskDO) {
        return activityTaskAsyncSupport.isAsync(activityTaskDO) && (chaosAppResponse.isSuccess()
                || isTimeOutError(chaosAppResponse));
    }

    private boolean isTimeOutError(ChaosAppResponse chaosAppResponse) {
        return chaosAppResponse.getOriginErrorMessage() != null && chaosAppResponse.getOriginErrorMessage().contains(
                ChaosBladeInvoker.TimeOutError);
    }

    private void saveTask(String appExecutionId, Throwable throwable,
                          ChaosAppResponse chaosAppResponse, MiniAppInvokeContext miniAppInvokeContext, String activityTaskId) {
        activityTargetExecutionResultRepository.findById(appExecutionId).ifPresent(
                experimentActivityTargetTaskDO -> {
                    experimentActivityTargetTaskDO.setTaskId(appExecutionId);
                    experimentActivityTargetTaskDO.setOriginRequest(
                            JSON.toJSONString(miniAppInvokeContext.getChaosAppRequest()));
                    experimentActivityTargetTaskDO.setOriginResponse(
                            JSON.toJSONString(chaosAppResponse));
                    experimentActivityTargetTaskDO.setState(StateEnum.FINISHED.getValue());
                    experimentActivityTargetTaskDO.setGmtEnd(new Date());
                    experimentActivityTargetTaskDO.setAgentCode(determineAgentCode(chaosAppResponse));
                    determineErrorCode(experimentActivityTargetTaskDO, chaosAppResponse, throwable);
                    determineResultStatus(throwable, appExecutionId, experimentActivityTargetTaskDO, chaosAppResponse);
                    fillData(experimentActivityTargetTaskDO, chaosAppResponse);
                    fillHost(experimentActivityTargetTaskDO, miniAppInvokeContext, activityTaskId);
                    boolean result = activityTargetExecutionResultRepository.update(experimentActivityTargetTaskDO);
                    if (!result) {
                        log.error("update activity target result failed,appExecutionId:" + appExecutionId);
                    }
                });

    }

    private Integer determineAgentCode(ChaosAppResponse chaosAppResponse) {
        if (chaosAppResponse instanceof ChaosBladeAppResponse) {
            ChaosBladeAppResponse chaosBladeAppResponse = (ChaosBladeAppResponse)chaosAppResponse;
            //如果是成功的结果，那么就不存agentCode防止页面出现排查方案
            if (chaosBladeAppResponse.isSuccess()) {
                return null;
            }
            Response chaosBladeResponse = chaosBladeAppResponse.getChaosBladeResponse();
            if (chaosBladeResponse != null) {
                return chaosBladeResponse.getCode();
            }
        }
        return null;
    }

    private void fillData(ExperimentMiniAppTaskDO updateExperimentMiniAppTaskDO,
                          ChaosAppResponse chaosAppResponse) {
        try {
            if (chaosAppResponse.getData() == null || chaosAppResponse.getData().isEmpty()) {
                updateExperimentMiniAppTaskDO.setData(null);
            } else {
                updateExperimentMiniAppTaskDO.setData(
                        JSON.toJSONString(chaosAppResponse.getData()));
            }
        } catch (Throwable error) {
            updateExperimentMiniAppTaskDO.setErrorMessage("serialize chaos app response data failed");
            log.error("serialize chaos app response data failed", error);
        }

    }

    private void determineResultStatus(Throwable throwable, String appExecutionId,
                                       ExperimentMiniAppTaskDO updateExperimentMiniAppTaskDO, ChaosAppResponse chaosAppResponse) {
        if (throwable != null) {
            RecordsRepo.getMiniAppInvocationRecorder().log("Invoke exception", true, throwable);
            updateExperimentMiniAppTaskDO.setResult(ResultEnum.ERROR.getValue());
            updateExperimentMiniAppTaskDO.setErrorMessage(
                    ExceptionHelper.detailedMessage(throwable));
        } else {
            updateExperimentMiniAppTaskDO.setResult(
                    chaosAppResponse.isSuccess() ? ResultEnum.SUCCESS.getValue() : ResultEnum.FAILED.getValue());
            updateExperimentMiniAppTaskDO.setErrorMessage(
                    chaosAppResponse.getErrorMessage());
        }
        if (updateExperimentMiniAppTaskDO.getErrorMessage() == null) {
            updateExperimentMiniAppTaskDO.setErrorMessage(CommonConstant.BLANK);
        }
    }

    private void determineErrorCode(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, ChaosAppResponse chaosAppResponse,
                                    Throwable throwable) {
        if (chaosAppResponse.isSuccess()) {
            experimentMiniAppTaskDO.setErrorCode(MiniAppInvokeStatusCode.SC_OK.name());
            return;
        }
        if (!Strings.isNullOrEmpty(chaosAppResponse.getErrorCode())) {
            experimentMiniAppTaskDO.setErrorCode(chaosAppResponse.getErrorCode());
            experimentMiniAppTaskDO.setErrorMessage(chaosAppResponse.getErrorMessage());
            return;
        }
        if (throwable instanceof ChaosException) {
            ChaosException ChaosException = (ChaosException)throwable;
            IErrorCode iErrorCode = ChaosException.getErrorCode();
            experimentMiniAppTaskDO.setErrorCode(iErrorCode.name());
            experimentMiniAppTaskDO.setErrorMessage(throwable.getMessage());
            return;
        }
        if (chaosAppResponse instanceof ChaosBladeAppResponse) {
            ChaosBladeAppResponse chaosBladeAppResponse = (ChaosBladeAppResponse)chaosAppResponse;
            MiniAppInvokeStatusCode miniAppInvokeStatusCode = MiniAppInvokeStatusCode.ofErrorStatus(
                    chaosBladeAppResponse.getStatus());
            experimentMiniAppTaskDO.setErrorCode(miniAppInvokeStatusCode.name());
        }
    }

    private void fillHost(ExperimentMiniAppTaskDO updateExperimentMiniAppTaskDO,
                          MiniAppInvokeContext miniAppInvokeContext, String activityTaskId) {
        Host host = miniAppInvokeContext.getHost();
        if (host != null) {
            updateExperimentMiniAppTaskDO.setHostIp(host.getIp());
            if (updateExperimentMiniAppTaskDO.isSuccess()) {
                log.info(LogFormats.Activity
                        .buildFinishedRunOnTarget(activityTaskId, miniAppInvokeContext.getHost(),
                                updateExperimentMiniAppTaskDO.isSuccess(),
                                updateExperimentMiniAppTaskDO.getErrorMessage()));
            } else {
                log.error(LogFormats.Activity
                        .buildFinishedRunOnTarget(activityTaskId, miniAppInvokeContext.getHost(),
                                updateExperimentMiniAppTaskDO.isSuccess(),
                                updateExperimentMiniAppTaskDO.getErrorMessage()));
            }

        }
    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE;
    }

}
