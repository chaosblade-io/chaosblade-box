package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.log.MiniAppInvocationRecordBuilder;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.RecordsRepo;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.AppCallback;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppExecutor;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

/**
 * @author haibin
 *
 *
 */
@Slf4j
public class ChaosAppInvoker {

    public static ChaosAppResponse invokeFromMiniApp(MiniAppInvokeContext miniAppInvokeContext) {
        String appCode = miniAppInvokeContext.getActivityInvokeContext().getExecutableAppCode();
        ChaosAppRequest chaosAppRequest = miniAppInvokeContext.getChaosAppRequest();
        ChaosAppContext chaosAppContext = miniAppInvokeContext.getStepExecuteContext().getChaosAppContext();
        StopWatch stopWatch = StopWatch.createStarted();
        ChaosAppResponse result = invoke(appCode, chaosAppRequest, chaosAppContext);
        stopWatch.stop();
        logInvocation(miniAppInvokeContext, appCode, chaosAppRequest, stopWatch, result);
        return result;
    }

    public static ChaosAppResponse invokeByGuard(ChaosAppContext chaosAppContext, ChaosAppRequest chaosAppRequest, String appCode,
                                              ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        StopWatch stopWatch = StopWatch.createStarted();
        ChaosAppResponse result = invoke(appCode, chaosAppRequest, chaosAppContext);
        stopWatch.stop();
        logInvocation(chaosAppRequest, appCode, experimentGuardInstanceDO, stopWatch, result);
        return result;
    }

    private static void logInvocation(MiniAppInvokeContext miniAppInvokeContext, String appCode,
                                      ChaosAppRequest chaosAppRequest, StopWatch stopWatch, ChaosAppResponse result) {
        try {
            MiniAppInvocationRecordBuilder miniAppInvocationRecordBuilder = MiniAppInvocationRecordBuilder.init(
                ChaosTraceUtil.generateTraceId(), chaosAppRequest, appCode);
            if (miniAppInvokeContext.getHost() != null) {
                Host host = miniAppInvokeContext.getHost();
                miniAppInvocationRecordBuilder.withIp(host.getIp());
                miniAppInvocationRecordBuilder.withConfigurationId(host.getAppConfigurationId());
            }
            ActivityTaskDO activityTaskDO = miniAppInvokeContext.getActivityInvokeContext().getActivityTask();
            miniAppInvocationRecordBuilder.withActivityTaskId(activityTaskDO.getTaskId());
            miniAppInvocationRecordBuilder.withExperimentTaskId(activityTaskDO.getExperimentTaskId());
            miniAppInvocationRecordBuilder.withMiniAppTaskId(miniAppInvokeContext.getMiniAppTaskId());
            RecordsRepo.getMiniAppInvocationRecorder().record(
                miniAppInvocationRecordBuilder.build(result.isSuccess(), result, stopWatch.getTime()));
        } catch (Exception ignore) {
            log.warn("record miniapp trace log failed", ignore);
        }
    }

    private static void logInvocation(ChaosAppRequest chaosAppRequest, String appCode,
                                      ExperimentGuardInstanceDO experimentGuardInstanceDO, StopWatch stopWatch, ChaosAppResponse result) {
        try {
            MiniAppInvocationRecordBuilder miniAppInvocationRecordBuilder = MiniAppInvocationRecordBuilder.init(
                ChaosTraceUtil.generateTraceId(), chaosAppRequest, appCode);
            miniAppInvocationRecordBuilder.withExperimentTaskId(experimentGuardInstanceDO.getExperimentTaskId());
            RecordsRepo.getMiniAppInvocationRecorder().record(
                miniAppInvocationRecordBuilder.build(result.isSuccess(), result, stopWatch.getTime()));
        } catch (Exception ignore) {
            log.warn("record miniapp trace log failed", ignore);
        }
    }

    private static ChaosAppResponse invoke(String appCode, ChaosAppRequest chaosAppRequest, ChaosAppContext chaosAppContext) {
        final ChaosAppResponse[] chaosAppResponse = {new ChaosAppResponse()};
        AppCallback callback = new AppCallback() {
            @Override
            public void agentAttached(Scope scope, PhaseType phase, ChaosAppResponse response) {
                chaosAppResponse[0] = response;
            }

            @Override
            public void agentAttachFailed(Scope scope, PhaseType phase, Throwable throwable) {
                chaosAppResponse[0].setErrorMessage(ExceptionHelper.detailedMessage(throwable));
            }

            @Override
            public void completed(Scope scope, PhaseType phase, ChaosAppResponse response) {
                chaosAppResponse[0] = response;
            }

            @Override
            public void failed(Scope scope, PhaseType phase, Throwable throwable) {
                if (throwable instanceof ChaosException) {
                    ChaosException ChaosException = (ChaosException)throwable;
                    chaosAppResponse[0].setErrorMessage(ChaosException.getMessage());
                    chaosAppResponse[0].setErrorCode(ChaosException.getErrorCode().name());
                } else {
                    chaosAppResponse[0].setErrorCode(CommonErrorCode.B_INVOKE_MINI_APP_FAILED.name());
                    chaosAppResponse[0].setErrorMessage(ExceptionHelper.detailedMessage(throwable));
                }
            }
        };
        ChaosAppExecutor.getInstance().execute(appCode, chaosAppRequest, chaosAppContext, callback);
        chaosAppResponse[0].setScope(chaosAppRequest.getScope());
        return chaosAppResponse[0];
    }

}
