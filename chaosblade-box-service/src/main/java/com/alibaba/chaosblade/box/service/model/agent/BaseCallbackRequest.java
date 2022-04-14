package com.alibaba.chaosblade.box.service.model.agent;

import com.alibaba.chaosblade.box.common.common.constant.PrivateCloudConstant;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class BaseCallbackRequest {

    @JSONField(name = PrivateCloudConstant.PARAM_REQUEST_ID)
    private String requestId;

    @JSONField(name = PrivateCloudConstant.PARAM_USERID)
    private String userId;

    @JSONField(name = PrivateCloudConstant.PARAM_CONFIGURATION)
    private String configurationId;

    @JSONField(name = PrivateCloudConstant.PARAM_DEVICE_CONFIGURATION)
    private String deviceConfigurationId;

    @JSONField(name = PrivateCloudConstant.PARAM_AK)
    private String ak;
}
