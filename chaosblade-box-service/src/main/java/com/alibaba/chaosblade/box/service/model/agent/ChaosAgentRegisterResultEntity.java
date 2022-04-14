package com.alibaba.chaosblade.box.service.model.agent;


import com.alibaba.chaosblade.box.common.common.constant.PrivateCloudConstant;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChaosAgentRegisterResultEntity {

    @JSONField(name = PrivateCloudConstant.PARAM_CONFIGURATION)
    private String configurationId;

    @JSONField(name = PrivateCloudConstant.PARAM_CHAOSBLADE_VERSION)
    private String chaosBladeVersion;

    @JSONField(name = PrivateCloudConstant.PARAM_CHAOSBLADE_MD5)
    private String chaosBladeMd5;

    @JSONField(name = PrivateCloudConstant.PARAM_USERID)
    private String uid;

    @JSONField(name = PrivateCloudConstant.PARAM_SK)
    private String sk;

    @JSONField(name = PrivateCloudConstant.PARAM_AK)
    private String ak;
}
