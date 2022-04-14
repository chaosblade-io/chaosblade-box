package com.alibaba.chaosblade.box.service.model.agent;

import com.alibaba.chaosblade.box.common.common.constant.PrivateCloudConstant;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


@Data
public class AgentMetricRequest extends BaseCallbackRequest {

    @JSONField(name = PrivateCloudConstant.PARAM_METRIC_MSGS)
    private String metrics;

    @JSONField(name = PrivateCloudConstant.PARAM_DEVICE_CONFIGURATION)
    private String did;

    @JSONField(name = PrivateCloudConstant.PARAM_USERID)
    private String uid;

    @JSONField(name = PrivateCloudConstant.PARAM_CHAOS_RESULT_STATUS)
    private String status;

    @JSONField(name = PrivateCloudConstant.PARAM_CHAOS_RESULT_ERROR_MSG)
    private String error;
}
