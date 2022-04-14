package com.alibaba.chaosblade.box.dao.infrastructure.model.cluster;

import lombok.Data;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class KubernetesPod {

    private String podName;

    private String kubNamespace;

    private String podIp;

    //chaos相关
    private String namespace;

    private String userId;

    private String clusterId;

    private String targetConfigurationId;
}
