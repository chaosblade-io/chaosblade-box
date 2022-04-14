package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp;


import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;

/**
 * @author haibin.lhb
 *
 *
 */
public class MiniAppInvokeFlowThreadLocalContext {

    private static final ThreadLocal<MiniAppInvokeFlowThreadLocalContext> INVOKE_CONTEXT = new ThreadLocal<>();
    private final boolean fromAsync;

    private String experimentTaskId;

    private String activityTaskId;

    private String appCode;

    private String miniAppTaskId;

    private String configurationId;

    private String requestId;

    public MiniAppInvokeFlowThreadLocalContext(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, boolean fromAsync) {
        this.experimentTaskId = experimentMiniAppTaskDO.getExperimentTaskId();
        this.activityTaskId = experimentMiniAppTaskDO.getActivityTaskId();
        this.appCode = experimentMiniAppTaskDO.getAppCode();
        this.miniAppTaskId = experimentMiniAppTaskDO.getTaskId();
        this.configurationId = experimentMiniAppTaskDO.getAppConfigurationId();
        this.fromAsync=fromAsync;
        this.requestId = this.miniAppTaskId;
    }

    public static void startMiniAppInvoke(ExperimentMiniAppTaskDO experimentMiniAppTaskDO,boolean fromAsync) {
        MiniAppInvokeFlowThreadLocalContext miniAppInvokeThreadLocalContext = new MiniAppInvokeFlowThreadLocalContext(experimentMiniAppTaskDO,fromAsync);
        INVOKE_CONTEXT.set(miniAppInvokeThreadLocalContext);
    }




    public String getRequestId() {
        return this.requestId;
    }

    public static void endMiniAppInvoke() {
        INVOKE_CONTEXT.remove();
    }

    public static MiniAppInvokeFlowThreadLocalContext getContext() {
        return INVOKE_CONTEXT.get();
    }

    public String getExperimentTaskId() {
        return this.experimentTaskId;
    }

    public String getActivityTaskId() {
        return this.activityTaskId;
    }

    public String getAppCode() {
        return this.appCode;
    }

    public String getConfigurationId() {
        return this.configurationId;
    }
}
