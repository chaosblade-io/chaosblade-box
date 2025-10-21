/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
