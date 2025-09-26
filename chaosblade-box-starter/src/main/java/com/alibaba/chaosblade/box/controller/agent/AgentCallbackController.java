package com.alibaba.chaosblade.box.controller.agent;

import com.alibaba.chaosblade.box.annotation.AgentEndPoint;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.controller.BaseController;
import com.alibaba.chaosblade.box.service.ChaosAgentCallbackService;
import com.alibaba.chaosblade.box.service.model.agent.*;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AgentCallbackController extends BaseController {

  @Autowired private ChaosAgentCallbackService chaosAgentCallbackService;

  @PostMapping(value = "AgentRegister")
  @AgentEndPoint
  public Response<ChaosAgentRegisterResultEntity> registerAgent(
      @RequestBody RegisteredCallbackRequest registeredCallbackRequest) {
    return chaosAgentCallbackService.handleAgentRegisterRequest(registeredCallbackRequest);
  }

  @PostMapping(value = "AgentHeartBeat")
  @AgentEndPoint
  public Response<Boolean> agentHeartBeat(
      @RequestBody HeartBeatCallbackRequest heartBeatCallbackRequest) {
    return chaosAgentCallbackService.handleAgentHeartBeat(heartBeatCallbackRequest);
  }

  @PostMapping(value = "AgentClosed")
  @AgentEndPoint
  public Response<Boolean> agentClosedCallback(
      @RequestBody AgentClosedCallbackRequest agentClosedCallbackRequest) {
    return chaosAgentCallbackService.handleAgentClosedRequest(agentClosedCallbackRequest);
  }

  @PostMapping(value = "javaAgentInstall")
  @AgentEndPoint
  public Response<String> javaAgentInstall(@RequestBody AgentMetricRequest agentMetricRequest) {
    return chaosAgentCallbackService.handleJavaAgentInstall(agentMetricRequest);
  }

  @PostMapping(value = "k8sPod")
  @AgentEndPoint
  public Response<Map<String, String>> k8sPod(
      @RequestBody K8sPodsMetricRequest k8sPodsMetricRequest) {

    return chaosAgentCallbackService.handlerPodMetric(k8sPodsMetricRequest);
  }

  @PostMapping(value = "chaosbladeAsync")
  @AgentEndPoint
  public Response<String> chaosbladeAsync(
      @RequestBody ChaosbladeAsyncRequest chaosbladeAsyncRequest) {

    return chaosAgentCallbackService.handlerChaosbladeAsync(chaosbladeAsyncRequest);
  }
}
