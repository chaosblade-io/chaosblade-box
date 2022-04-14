package com.alibaba.chaosblade.box.dao.model.base;

import lombok.Data;

import java.util.Date;

@Data
public class AgentVersionDO {
    private String pluginType;
    private String version;
    private String parentPluginType;
    private String parentVersion;
    private Boolean latest;
    private Date uptime;
    private String md5;
    private String stage;
    private String description;
    private String grayStrategy;
    private String grayTag;
    private String upgradeStrategy;
    private Double coverage;
    private String optId;
    private String optNick;
    private Integer osType;
}
