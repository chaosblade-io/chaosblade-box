package com.alibaba.chaosblade.box.service.model.agent;

import com.alibaba.chaosblade.box.common.common.constant.PrivateCloudConstant;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * agent的注册请求
 */
@Data
public class RegisteredCallbackRequest extends BaseCallbackRequest {

    /**
     * ip地址
     */
    @JSONField(name = PrivateCloudConstant.PARAM_AGENT_IP)
    private String ip;

    @JSONField(name = PrivateCloudConstant.PARAM_AGENT_PORT)
    private int port;

    @JSONField(name = "cpuNum")
    private int cpuNum;

    @JSONField(name = "memSize")
    private String memSize;

    /**
     * pid信息
     */
    @JSONField(name = PrivateCloudConstant.PARAM_AGENT_PROCESS_ID)
    private String pid;

    private String osVersion;

    @JSONField(name = "uptime")
    private String upTime;

    @JSONField(name = "instanceId")
    private String instanceId;

    /**
     * 设备
     */
    @JSONField(name = "deviceId")
    private String deviceId;

    @JSONField(name = "deviceType")
    private int deviceType;

    @JSONField(name = PrivateCloudConstant.PARAM_VERSION)
    private String version;

    @JSONField(name = "namespace")
    private String namespace;

    @JSONField(name = "osType")
    private String osType;

    @JSONField(name = "vpcId")
    private String vpcId;

    /**
     * appName
     */
    @JSONField(name = "appInstance")
    private String appName;

    @JSONField(name = "appGroup")
    private String appGroup;

    /**
     * k8s
     */
    @JSONField(name = "clusterId")
    private String clusterId;

    @JSONField(name = "clusterName")
    private String clusterName;

    /**
     * mode
     */
    @JSONField(name = "startupMode")
    private String startupMode;

    @JSONField(name = "agentMode")
    private String agentMode;
}
