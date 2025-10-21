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

package com.alibaba.chaosblade.box;

import com.alibaba.chaosblade.box.common.sdk.AgentForChaos;
import com.alibaba.chaosblade.box.common.sdk.ChaosBladeForCloud;
import com.alibaba.chaosblade.box.common.sdk.LitmusChaosForCloud;
import com.alibaba.chaosblade.box.common.sdk.channel.PaasTransportService;
import com.alibaba.chaosblade.box.common.sdk.channel.TransportServiceFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.MiniAppInvokeFlowThreadLocalContext;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CloudConfiguration {

  @Bean
  public ChaosBladeForCloud chaosBladeForCloud() {
    PaasTransportService transportService = TransportServiceFactory.createPassService(30000);
    return new ChaosBladeForCloud(
        transportService,
        () -> {
          MiniAppInvokeFlowThreadLocalContext miniAppInvokeThreadLocalContext =
              MiniAppInvokeFlowThreadLocalContext.getContext();
          if (miniAppInvokeThreadLocalContext != null) {
            return miniAppInvokeThreadLocalContext.getRequestId();
          }
          return UUID.randomUUID().toString();
        });
  }

  @Bean
  public LitmusChaosForCloud litmusChaosForCloud() {
    PaasTransportService transportService = TransportServiceFactory.createPassService(30000);
    return new LitmusChaosForCloud(transportService);
  }

  @Bean
  public AgentForChaos agentForChaos() {
    PaasTransportService transportService = TransportServiceFactory.createPassService(30000);
    return new AgentForChaos(transportService);
  }
}
