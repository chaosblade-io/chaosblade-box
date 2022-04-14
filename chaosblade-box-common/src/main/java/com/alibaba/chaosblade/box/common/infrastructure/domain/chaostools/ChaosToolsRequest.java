package com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChaosToolsRequest {

    private Long id;

    private Long deviceId;

    /**
     * tools name
     */
    private String name;

    /**
     * version
     */
    private String version;

    /**
     * download url
     */
    private String url;

    /**
     * device type
     */
    private Byte deviceType;

    private String toolsNamespace;

    private String installMode;

    @Deprecated
    private String configurationId;

    @Deprecated
    private String clusterId;

    /**
     * 操作ID，根据installMode判断是configurationId还是clusterId
     * installMode：host  | configurationId
     * installMode：k8s   | clusterId
     */
    private String operateId;

    private String userId;
}
