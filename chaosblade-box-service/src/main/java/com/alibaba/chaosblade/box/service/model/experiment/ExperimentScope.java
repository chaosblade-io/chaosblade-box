package com.alibaba.chaosblade.box.service.model.experiment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentScope implements Serializable {

    private String privateIp;

    private String publicIp;

    private Integer agentStatus;

    private Long connectTime;

    private String configurationId;

    private String hostConfigurationId;

    /**
     * 是否被演练
     */
    private Boolean isExperimented;

    private Integer experimentTaskCount;

    private String agentVersion;

    private String deviceId;

    private String hostName;

    private String clusterName;

    private List<String> groups;

    private String deviceName;

    private Integer deviceType;

    private List<String> deviceTags;

    private Integer osType;

    private List<String> chaosTools;

    private String pluginType;

    /**
     * 插件状态
     */
    private Integer pluginStatus;

    /**
     * 插件是否开启
     */
    private Boolean enable;

    /**
     * 是否可以白屏化安装卸载
     */
    private Boolean canAutoInstall;

    /**
     * 安装模式
     */
    private String installMode;

}
