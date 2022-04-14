package com.alibaba.chaosblade.box.common.sdk;

import com.alibaba.chaosblade.box.common.sdk.channel.PaasTransportService;
import com.alibaba.chaosblade.box.common.sdk.util.RequestUtil;
import com.alibaba.chaosblade.box.common.sdk.transport.Request;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;

public class AgentForChaos {
    private PaasTransportService transportService;

    public AgentForChaos(PaasTransportService transportService) {
        this.transportService = transportService;
    }
    public Response<String> uninstallAgent(String scope, String port, String handler) {

        Request request = RequestUtil.createRequest(scope, port, handler, "");
        return transportService.invoke(request, String.class);

    }


}
