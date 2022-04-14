package com.alibaba.chaosblade.box.service.model.agent;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PluginDTO implements Serializable {
    private static final long serialVersionUID = -1L;
    private String instanceId;
    private String instanceName;
    private String pluginType;
    private String appName;
    private Integer pluginStatus;
    private String publicIp;
    private String privateIp;
    private Boolean enable;
    private String configurationId;
    private Boolean canAutoInstall;
    private Boolean canAutoUnInstall;
    private Long createTime;
    private Integer osType;
    private String networkType;
    private String version;
    private String installMode;
    private Long connectTime;
    private String link;
    private Boolean upgrade;
    private String upgradeVersion;
    private List<String> chaosTools;
}
