package com.alibaba.chaosblade.box.dao.infrastructure.monitor.log;

import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.log.IRecord;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.log.MiniAppInvocationRecord;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.MiniAppInvokeFlowThreadLocalContext;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 小程序调用记录
 *
 * @author haibin
 * 
 *
 */
public class MiniAppInvocationRecorder implements IRecord<MiniAppInvocationRecord> {

    private final Logger logger = LoggerFactory.getLogger("mini_app_invoke_log");

    private final Logger traceLogger = LoggerFactory.getLogger("mini_app_trace_log");

    /**
     * 用来记录远程调用,日志格式:
     * 到cost为止必须有,experimentTaskId后面不一定有
     * traceId|appCode|miniappType|success|request|response|cost|hostIp|hostConfigurationId|experimentTaskId
     * |activityTaskId|miniAppTaskId
     *
     * @param recordObject
     */
    @Override
    public void record(MiniAppInvocationRecord recordObject) {
        try {
            recordObject.setCreate(new Date());
            logger.info("{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}", recordObject.getTraceId(), recordObject.getAppCode(),
                recordObject.getMiniAppType(), recordObject.isSuccess(),
                JSON.toJSONString(recordObject.getRequest()), JSON.toJSONString(recordObject.getResponse()),
                recordObject.getCost(), recordObject.getIp(), recordObject.getConfigurationId(),
                recordObject.getExperimentTaskId(), recordObject.getActivityTaskId(), recordObject.getMiniAppTaskId());
        } catch (Exception ex) {

        }
    }

    /**
     * 这个只是纯粹
     *
     * @param message
     * @param throwable
     */
    public void log(String message, boolean isPublic, Throwable throwable) {
        MiniAppInvokeFlowThreadLocalContext miniAppInvokeFlowThreadLocalContext
            = MiniAppInvokeFlowThreadLocalContext.getContext();
        if (miniAppInvokeFlowThreadLocalContext == null) {return;}
        if (throwable == null) {
            log(
                miniAppInvokeFlowThreadLocalContext.getRequestId(), isPublic,
                miniAppInvokeFlowThreadLocalContext.getConfigurationId(),
                miniAppInvokeFlowThreadLocalContext.getExperimentTaskId(),
                miniAppInvokeFlowThreadLocalContext.getActivityTaskId(),
                miniAppInvokeFlowThreadLocalContext.getAppCode(), message);
        } else {
            log(
                miniAppInvokeFlowThreadLocalContext.getRequestId(), isPublic,
                miniAppInvokeFlowThreadLocalContext.getConfigurationId(),
                miniAppInvokeFlowThreadLocalContext.getExperimentTaskId(),
                miniAppInvokeFlowThreadLocalContext.getActivityTaskId(),
                miniAppInvokeFlowThreadLocalContext.getAppCode(), message, throwable);
        }
    }

    /**
     * @param message
     */
    public void log(String miniAppTaskId, boolean isPublic, String configurationId, String experimentTaskId,
        String activityTaskId,
        String code, String message, Throwable throwable) {
        Throwable cause = ExceptionHelper.getRootCause(throwable);
        ExceptionHelper.hideChaosStack(cause);
        traceLogger.error(
            "miniAppTaskId={},public={},configurationId={},experimentTaskId={},activityTaskId={},code={},message={}",
            miniAppTaskId,
            isPublic,
            configurationId,
            experimentTaskId,
            activityTaskId,
            code, message
            , throwable);
    }

    /**
     * 这个只是纯粹
     *
     * @param message
     */
    public void log(String miniAppTaskId, boolean isPublic, String configurationId, String experimentTaskId,
        String activityTaskId,
        String code, String message) {
        traceLogger.info(
            "miniAppTaskId={},public={},configurationId={},experimentTaskId={},activityTaskId={},code={},message={}",
            miniAppTaskId,
            isPublic,
            configurationId,
            experimentTaskId,
            activityTaskId,
            code, message);
    }

    public void log(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, String message, boolean isPublic) {
        log(
            experimentMiniAppTaskDO.getTaskId(),
            isPublic,
            experimentMiniAppTaskDO.getAppConfigurationId(),
            experimentMiniAppTaskDO.getExperimentTaskId(),
            experimentMiniAppTaskDO.getActivityTaskId(),
            experimentMiniAppTaskDO.getAppCode(), message);
    }

    public void log(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, String message, boolean isPublic,
        Throwable throwable) {
        log(
            experimentMiniAppTaskDO.getTaskId(),
            isPublic,
            experimentMiniAppTaskDO.getAppConfigurationId(),
            experimentMiniAppTaskDO.getExperimentTaskId(),
            experimentMiniAppTaskDO.getActivityTaskId(),
            experimentMiniAppTaskDO.getAppCode(), message, throwable);
    }

    /**
     * 这个只是纯粹
     *
     * @param message
     */
    public void log(String message, boolean isPublic) {
        log(message, isPublic, null);
    }

    /**
     * 这个只是纯粹
     *
     * @param message
     */
    public void log(String message) {
        log(message, true, null);
    }

}
