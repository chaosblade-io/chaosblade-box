package com.alibaba.chaosblade.box.common.infrastructure.monitor.log;


import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;

import java.util.Map;

/**
 * @author haibin.lhb
 *
 *
 */
public final class MiniAppInvocationRecordBuilder {
    private MiniAppInvocationRecord miniAppInvocationRecord;

    private MiniAppInvocationRecordBuilder() { miniAppInvocationRecord = new MiniAppInvocationRecord(); }

    public static MiniAppInvocationRecordBuilder aMiniAppInvocationRecord() { return new MiniAppInvocationRecordBuilder(); }

    public static MiniAppInvocationRecordBuilder init(String requestId,
        ChaosAppRequest chaosAppRequest,
        String appCode) {
        return MiniAppInvocationRecordBuilder.aMiniAppInvocationRecord()
            .withTraceId(requestId)
            .withAppCode(appCode)
            .withRequest(chaosAppRequest)
            .withMiniAppType(MiniAppInvocationRecord.MINI_APP_TYPE_CHAOS_APP)
            .withAppCode(appCode)
            .withRequest(chaosAppRequest);
    }

    public MiniAppInvocationRecordBuilder withTraceId(String traceId) {
        miniAppInvocationRecord.setTraceId(traceId);
        return this;
    }

    public MiniAppInvocationRecordBuilder withAppCode(String appCode) {
        miniAppInvocationRecord.setAppCode(appCode);
        return this;
    }

    public MiniAppInvocationRecordBuilder withExperimentTaskId(String experimentTaskId) {
        miniAppInvocationRecord.setExperimentTaskId(experimentTaskId);
        return this;
    }

    public MiniAppInvocationRecordBuilder withActivityTaskId(String activityTaskId) {
        miniAppInvocationRecord.setActivityTaskId(activityTaskId);
        return this;
    }

    public MiniAppInvocationRecordBuilder withMiniAppTaskId(String miniAppTaskId) {
        miniAppInvocationRecord.setMiniAppTaskId(miniAppTaskId);
        return this;
    }

    public MiniAppInvocationRecordBuilder withMiniAppType(Integer miniAppType) {
        miniAppInvocationRecord.setMiniAppType(miniAppType);
        return this;
    }

    public MiniAppInvocationRecordBuilder withIp(String ip) {
        miniAppInvocationRecord.setIp(ip);
        return this;
    }

    public MiniAppInvocationRecordBuilder withConfigurationId(String configurationId) {
        miniAppInvocationRecord.setConfigurationId(configurationId);
        return this;
    }

    public MiniAppInvocationRecordBuilder withRequest(Object request) {
        miniAppInvocationRecord.setRequest(request);
        return this;
    }

    public MiniAppInvocationRecordBuilder withExtraInfo(Map<String, Object> extraInfo) {
        miniAppInvocationRecord.setExtraInfo(extraInfo);
        return this;
    }

    public MiniAppInvocationRecord build(boolean success, Object response, Long cost) {
        miniAppInvocationRecord.setResponse(response);
        miniAppInvocationRecord.setSuccess(success);
        miniAppInvocationRecord.setCost(cost);
        return miniAppInvocationRecord;
    }
}
