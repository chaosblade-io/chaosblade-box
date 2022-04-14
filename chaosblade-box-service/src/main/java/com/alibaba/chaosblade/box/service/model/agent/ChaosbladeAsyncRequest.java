package com.alibaba.chaosblade.box.service.model.agent;

import com.alibaba.chaosblade.box.common.common.constant.PrivateCloudConstant;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChaosbladeAsyncRequest  extends BaseCallbackRequest {
    @JSONField(name = PrivateCloudConstant.PARAM_CHAOS_RESULT_STATUS)
    private String status;

    @JSONField(name = PrivateCloudConstant.PARAM_CHAOS_RESULT_ERROR_MSG)
    private String error;

    @JSONField(name = PrivateCloudConstant.PARAM_CHAOS_TOOL_TYPE)
    private String toolType;

}
