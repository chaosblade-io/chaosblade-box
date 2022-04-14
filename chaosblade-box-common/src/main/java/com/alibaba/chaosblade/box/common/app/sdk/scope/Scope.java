package com.alibaba.chaosblade.box.common.app.sdk.scope;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author sunju
 *
 */
@Data
public class Scope implements Serializable {

    public static final Integer SCOPE_TYPE_HOST = 0;

    public static final Integer SCOPE_TYPE_CONTAINER = 1;

    public static final Integer SCOPE_TYPE_K8S = 2;

    //####################Start 普通机器信息###########################################################################
    /**
     * 是主机还是ECS
     * host 代表主机
     * ecs 代表ECS
     */
    String type;

    public Scope(String type, String ip, Integer port) {
        this.type = type;
        this.ip = ip;
        this.port = port;
    }

    /**
     * 原始的IP,对于应用来说,就是指应用的ip
     */
    String ip;

    /**
     * 端口
     */
    Integer port;
    /**
     * 主机名
     */
    String hostName;
    //####################End 普通机器信息###########################################################################

    //####################Start 机器信息###########################################################################
    /**
     * 设备VPC ID
     */
    String vpcId;

    /**
     * 设备私网IP
     */
    String privateIp;

    /**
     * 设备ID
     */
    String deviceId;

    /**
     * 设备名
     */
    String deviceName;

    /**
     * 当前设备的configurationId,如果是k8s的应用类型，那么这里的configuration是Agent所在的configurationId,
     * 而不是业务pod的，业务pod记录在appConfigurationId字段里面
     */
    String deviceConfigurationId;
    /**
     * 机器类型
     *
     */
    private Integer scopeType;

    /**
     * 设备类型
     *
     */
    private Integer deviceType;
    //####################End 机器信息###########################################################################

    //####################Start 应用信息###########################################################################

    /**
     * 应用名
     */
    String app;

    /**
     * 所属应用ID
     */
    String appId;
    /**
     * 分组名
     */
    String nodeGroup;
    /**
     * 应用的configurationId,大部分情况下这个值和configurationId是一样的，但是对于k8s应用，业务的设备ID和我们故障注入的operator设备ID不一样,所以这两个值可能不一样
     */
    private String appConfigurationId;
    //####################End 应用信息###########################################################################

    //####################Start 云服务信息###########################################################################

    String cloudServiceType;

    //####################End 云服务信息###########################################################################

    //####################Start K8S信息###########################################################################
    /**
     * k8s集群ID
     */
    String clusterId;

    /**
     * 集群名字
     */
    String clusterName;

    /**
     * 是否是k8s
     */
    boolean k8s;

    /**
     * 是否是master节点
     */
    boolean master;

    String kubNamespace;

    //####################End K8S信息###########################################################################

    /**
     * 当前用户是否允许操作该资源
     */
    boolean allow;

    Integer osType;

    public String getTargetIp() {
        if (targetIp == null) {
            return ip;
        }
        return targetIp;
    }

    /**
     * 目标机器的IP
     */
    private String targetIp;

    public Long getLongAppId() {
        if (appId == null || appId.isEmpty()) { return null; }
        return Long.valueOf(appId);
    }

    public boolean isAppScope() {
        return app != null && !app.isEmpty();
    }

    protected Scope(String ip, Integer port) {
        this.type = type;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Scope{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", port=").append(port);
        sb.append(", hostName='").append(hostName).append('\'');
        sb.append(", app='").append(app).append('\'');
        sb.append(", nodeGroup='").append(nodeGroup).append('\'');
        sb.append(", deviceId='").append(deviceId).append('\'');
        sb.append(", deviceConfigurationId='").append(deviceConfigurationId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Scope)) { return false; }
        Scope scope = (Scope)o;
        return Objects.equals(getIp(), scope.getIp()) &&
            Objects.equals(getHostName(), scope.getHostName()) &&
            Objects.equals(getDeviceConfigurationId(), scope.getDeviceConfigurationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIp(), getHostName(), getDeviceConfigurationId());
    }
}
