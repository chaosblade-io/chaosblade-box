package com.alibaba.chaosblade.box.service.model.agent;

import com.alibaba.chaosblade.box.common.common.constant.PrivateCloudConstant;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * agent的心跳请求
 */
@Data
public class HeartBeatCallbackRequest extends BaseCallbackRequest {

    /**
     * 版本号
     */
    @JSONField(name = PrivateCloudConstant.PARAM_VERSION)
    private String version;

    @JSONField(name = PrivateCloudConstant.PARAM_CHAOSBLADE_VERSION)
    private String chaosVersion;

    @JSONField(name = PrivateCloudConstant.PARAM_AGENT_PROCESS_ID)
    private String pid;

    @JSONField(name = PrivateCloudConstant.PARAM_AGENT_IP)
    private String ip;

}
