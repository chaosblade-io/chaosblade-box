package com.alibaba.chaosblade.box.service.model.agent;

import com.alibaba.chaosblade.box.common.common.constant.PrivateCloudConstant;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class K8sPodsMetricRequest extends BaseCallbackRequest {
    @JSONField(name = PrivateCloudConstant.PARAM_SK)
    private String sk;

    @JSONField(name = PrivateCloudConstant.PARAM_AGENT_PROCESS_ID)
    private String pid;

    @JSONField(name = PrivateCloudConstant.PARAM_AGENT_PORT)
    private String port;

    @JSONField(name = PrivateCloudConstant.PARAM_VERSION)
    private String version;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "pods")
    private String pods;
}
