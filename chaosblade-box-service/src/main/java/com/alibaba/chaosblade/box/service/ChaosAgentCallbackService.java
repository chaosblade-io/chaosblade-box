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
  public Response<Boolean> handleAgentClosedRequest(
      AgentClosedCallbackRequest agentClosedCallbackRequest);

  public Response<String> handleJavaAgentInstall(AgentMetricRequest agentMetricRequest);

  public Response<Map<String, String>> handlerPodMetric(K8sPodsMetricRequest k8sPodsMetricRequest);

  public Response<String> handlerChaosbladeAsync(ChaosbladeAsyncRequest chaosbladeAsyncRequest);
}
