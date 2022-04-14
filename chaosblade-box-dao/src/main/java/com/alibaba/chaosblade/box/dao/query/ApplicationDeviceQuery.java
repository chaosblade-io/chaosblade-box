package com.alibaba.chaosblade.box.dao.query;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ApplicationDeviceQuery {

    /**
     * 应用Id
     */
    private Long appId;
    /**
     * 分组名
     */
    private String groupName;

    /**
     * 设备关键词
     */
    private String partName;

    private String userId;

    private String namespace;

    /**
     * 应用名
     */
    private String appName;

    /**
     * private ip
     */
    private String ip;

    private List<String> groups;

    private List<String> tags;

    private List<String> kubNamespaces;

    private List<String> ips;

    private Integer status;

    private List<Integer> dimensions;

    private String hostConfigurationId;

    private Integer osType;

    private String clusterId;

    private List<String> clusterIds;

}
