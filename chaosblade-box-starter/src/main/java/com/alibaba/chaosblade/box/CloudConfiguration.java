package com.alibaba.chaosblade.box;

import com.alibaba.chaosblade.box.common.sdk.AgentForChaos;
import com.alibaba.chaosblade.box.common.sdk.ChaosBladeForCloud;
import com.alibaba.chaosblade.box.common.sdk.LitmusChaosForCloud;
import com.alibaba.chaosblade.box.common.sdk.channel.PaasTransportService;
import com.alibaba.chaosblade.box.common.sdk.channel.TransportServiceFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.MiniAppInvokeFlowThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@Slf4j
public class CloudConfiguration {

    @Bean
    public ChaosBladeForCloud chaosBladeForCloud() {
        PaasTransportService transportService = TransportServiceFactory.createPassService(30000);
        return new ChaosBladeForCloud(transportService, () -> {
            MiniAppInvokeFlowThreadLocalContext miniAppInvokeThreadLocalContext
                    = MiniAppInvokeFlowThreadLocalContext.getContext();
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
