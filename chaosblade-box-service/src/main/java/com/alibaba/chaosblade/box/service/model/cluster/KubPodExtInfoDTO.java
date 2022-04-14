package com.alibaba.chaosblade.box.service.model.cluster;

import lombok.Data;

import java.io.Serializable;

@Data
public class KubPodExtInfoDTO implements Serializable {
    private static final long serialVersionUID = -1L;
    private String userId;
    private String configurationId;
    private String uid;
    private String namespace;
    private String name;
    private String clusterId;
    private String clusterName;
    private String kubNamespace;
    private String state;
    private String type;
    private String labels;
    private Integer restartCount;
    private String uptime;
    private String ip;
    private Integer containerCount;
    private String hostConfigurationId;
    private String daemonSetUid;
    private String daemonSetCid;
    private String replicaSetUid;
    private String replicaSetCid;
    private String deploymentUid;
    private String deploymentCid;
    private String serviceUid;
    private String serviceCid;
    private String md5;

}
