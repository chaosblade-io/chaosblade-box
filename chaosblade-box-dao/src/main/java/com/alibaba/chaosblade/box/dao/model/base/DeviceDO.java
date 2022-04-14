package com.alibaba.chaosblade.box.dao.model.base;

import java.util.StringJoiner;

/**
 * @author changjun.xcj
 *
 */
public class DeviceDO extends BaseDO {

    private String userId;

    private String namespace;

    private String vpcId;

    private String provider;

    /**
     * 如果是k8s，此字段存储k8s的集群id
     */
    private String clusterId;

    /**
     * 集群名称
     */
    private String clusterName;

    private String version;

    private String chaosVersion;

    private String publicIp;

    private String privateIp;

    private String parentIp;

    private Integer port;

    /**
     * 对于ecs主机，存储instanceId
     */
    private String deviceId;

    private Integer deviceType;

    private String deviceName;

    private String deviceRole;

    private String serialNumber;

    private String osVersion;

	/**
	 * os 类型
	 * linux / windows
	 */
	private Integer osType;

    private String hostname;

	/**
	 * 交换机ID
	 */
	private String vswitchId;

    /**
     * 实例规格
     */
    private String spec;

    /**
     * CPU
     */
    private Integer cpu;

    /**
     * MEM,单位M KB
     */
    private Integer mem;

    private String configurationId;

    /**
     * 主机配置ID
     */
    private String hostConfigurationId;

    /**
     * 主机实例ID
     */
    private String hostInstanceId;

    /**
     * 父设备ID
     */
    private String parentConfigurationId;

    /**
     * 父设备名称
     */
    private String parentDeviceName;

    /**
     * 父设备类型
     */
    private Integer parentDeviceType;

    /**
     * 父设备命名空间，如果是k8s，对应k8s的namespace
     */
    private String parentDeviceSpace;

    private Integer status;

    private Boolean enable;

    private String installMode;

    /**
     * 设备创建时间，用于识别设备是否支持云助手通道的安装流程
     */
    private String deviceCreateTime;

    /**
     * 设备启动时间
     */
    private String uptime;

    /**
     * 设备连接时间
     */
    private Long connectTime;

    /**
     * 最近一次对设备执行的命令ID，用于云助手命令执行
     */
    private String commandId;

    /**
     * 最近一次安装或者卸载请求的请求id，用于查询安装状态
     */
    private String requestId;

    /**
     * 命令执行时间
     */
    private Long commandTime;

    /**
     * 请求结果
     */
    private String requestResult;

    /**
     * 故障原因
     */
    private String reason;

    /**
     * 故障原因编码
     */
    private String reasonCode;

    /**
     * 设备元信息md5，用于表示设备信息是否变化
     */
    private String md5;

    /**
     * 扩展信息
     */
    private String extInfo;

    private Long lastHealthPingTime;

    private Long lastOnlineTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChaosVersion() {
        return chaosVersion;
    }

    public void setChaosVersion(String chaosVersion) {
        this.chaosVersion = chaosVersion;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

//    public String getRegionId() {
//        return regionId;
//    }
//
//    public void setRegionId(String regionId) {
//        this.regionId = regionId;
//    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public Long getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(Long connectTime) {
        this.connectTime = connectTime;
    }

    public String getParentIp() {
        return parentIp;
    }

    public void setParentIp(String parentIp) {
        this.parentIp = parentIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceRole() {
        return deviceRole;
    }

    public void setDeviceRole(String deviceRole) {
        this.deviceRole = deviceRole;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public String getParentConfigurationId() {
        return parentConfigurationId;
    }

    public void setParentConfigurationId(String parentConfigurationId) {
        this.parentConfigurationId = parentConfigurationId;
    }

    public String getHostConfigurationId() {
        return hostConfigurationId;
    }

    public void setHostConfigurationId(String hostConfigurationId) {
        this.hostConfigurationId = hostConfigurationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Long getLastHealthPingTime() {
        return lastHealthPingTime;
    }

    public void setLastHealthPingTime(Long lastHealthPingTime) {
        this.lastHealthPingTime = lastHealthPingTime;
    }

    public void setLastOnlineTime(Long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getCommandTime() {
        return commandTime;
    }

    public void setCommandTime(Long commandTime) {
        this.commandTime = commandTime;
    }

    public String getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(String requestResult) {
        this.requestResult = requestResult;
    }

    public String getDeviceCreateTime() {
        return deviceCreateTime;
    }

    public void setDeviceCreateTime(String deviceCreateTime) {
        this.deviceCreateTime = deviceCreateTime;
    }

    public Long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getParentDeviceName() {
        return parentDeviceName;
    }

    public void setParentDeviceName(String parentDeviceName) {
        this.parentDeviceName = parentDeviceName;
    }

    public Integer getParentDeviceType() {
        return parentDeviceType;
    }

    public void setParentDeviceType(Integer parentDeviceType) {
        this.parentDeviceType = parentDeviceType;
    }

    public String getParentDeviceSpace() {
        return parentDeviceSpace;
    }

    public void setParentDeviceSpace(String parentDeviceSpace) {
        this.parentDeviceSpace = parentDeviceSpace;
    }

    public String getInstallMode() {
        return installMode;
    }

    public void setInstallMode(String installMode) {
        this.installMode = installMode;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getHostInstanceId() {
        return hostInstanceId;
    }

    public void setHostInstanceId(String hostInstanceId) {
        this.hostInstanceId = hostInstanceId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMem() {
        return mem;
    }

    public void setMem(Integer mem) {
        this.mem = mem;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

	public String getVswitchId() {
		return vswitchId;
	}

	public void setVswitchId(String vswitchId) {
		this.vswitchId = vswitchId;
	}

	public Integer getOsType() {
		return osType;
	}

	public void setOsType(Integer osType) {
		this.osType = osType;
	}

	public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getPort() {
        return this.port;
    }

	@Override
	public String toString() {
		return new StringJoiner(", ", DeviceDO.class.getSimpleName() + "[", "]")
			.add("userId='" + userId + "'")
			.add("namespace='" + namespace + "'")
			.add("vpcId='" + vpcId + "'")
			.add("provider='" + provider + "'")
//			.add("regionId='" + regionId + "'")
			.add("clusterId='" + clusterId + "'")
			.add("clusterName='" + clusterName + "'")
			.add("version='" + version + "'")
			.add("chaosVersion='" + chaosVersion + "'")
			.add("publicIp='" + publicIp + "'")
			.add("privateIp='" + privateIp + "'")
			.add("parentIp='" + parentIp + "'")
            .add("port='" + port + "'")
			.add("deviceId='" + deviceId + "'")
			.add("deviceType=" + deviceType)
			.add("deviceName='" + deviceName + "'")
			.add("deviceRole='" + deviceRole + "'")
			.add("serialNumber='" + serialNumber + "'")
			.add("osVersion='" + osVersion + "'")
			.add("osType='" + osType + "'")
			.add("hostname='" + hostname + "'")
			.add("vswitchId='" + vswitchId + "'")
			.add("spec='" + spec + "'")
			.add("cpu=" + cpu)
			.add("mem=" + mem)
			.add("configurationId='" + configurationId + "'")
			.add("hostConfigurationId='" + hostConfigurationId + "'")
			.add("hostInstanceId='" + hostInstanceId + "'")
			.add("parentConfigurationId='" + parentConfigurationId + "'")
			.add("parentDeviceName='" + parentDeviceName + "'")
			.add("parentDeviceType=" + parentDeviceType)
			.add("parentDeviceSpace='" + parentDeviceSpace + "'")
			.add("status=" + status)
			.add("enable=" + enable)
			.add("installMode='" + installMode + "'")
			.add("deviceCreateTime='" + deviceCreateTime + "'")
			.add("uptime='" + uptime + "'")
			.add("connectTime=" + connectTime)
			.add("commandId='" + commandId + "'")
			.add("requestId='" + requestId + "'")
			.add("commandTime=" + commandTime)
			.add("requestResult='" + requestResult + "'")
			.add("reason='" + reason + "'")
			.add("reasonCode='" + reasonCode + "'")
			.add("md5='" + md5 + "'")
			.add("extInfo='" + extInfo + "'")
			.add("lastHealthPingTime=" + lastHealthPingTime)
			.add("lastOnlineTime=" + lastOnlineTime)
			.toString();
	}
}
