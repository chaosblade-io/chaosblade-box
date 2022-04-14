package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.service.model.agent.*;

import java.util.Map;

public interface ChaosAgentCallbackService {

    /**
     * 处理客户端心跳
     *
     * @param heartBeatCallbackRequest
     * @return
     */
    public Response<Boolean> handleAgentHeartBeat(HeartBeatCallbackRequest heartBeatCallbackRequest);

    /**
     * 处理客户端注册
     *
     * @param registeredCallbackRequest
     * @return
     */
    public Response<ChaosAgentRegisterResultEntity> handleAgentRegisterRequest(
        RegisteredCallbackRequest registeredCallbackRequest);

    /**
     * 处理客户端关闭回调
     *
     * @param agentClosedCallbackRequest
     * @return
     */
    public Response<Boolean> handleAgentClosedRequest(AgentClosedCallbackRequest agentClosedCallbackRequest);

    public Response<String> handleJavaAgentInstall(AgentMetricRequest agentMetricRequest);

    public Response<Map<String, String>>  handlerPodMetric(K8sPodsMetricRequest k8sPodsMetricRequest);

    public Response<String>  handlerChaosbladeAsync(ChaosbladeAsyncRequest chaosbladeAsyncRequest);

}
