package com.alibaba.chaosblade.box.dao.infrastructure.model.cluster;

import lombok.Data;

/**
 * @author haibin.lhb
 *
 * 
 */
@Data

public class KubernetesCluster {

    public KubernetesCluster(String clusterName, String clusterId, String vpcId) {
        this.clusterName = clusterName;
        this.clusterId = clusterId;
        this.vpcId = vpcId;
    }

    private String clusterName;

    private String clusterId;

    private String vpcId;


}
