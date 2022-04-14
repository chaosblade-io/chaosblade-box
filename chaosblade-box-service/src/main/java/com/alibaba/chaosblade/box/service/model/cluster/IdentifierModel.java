package com.alibaba.chaosblade.box.service.model.cluster;

import lombok.Data;
import lombok.ToString;

/**
 * @author: xinyuan.ymm
 * @create: 2021-10-12 7:14 下午
 */
@ToString
@Data
public class IdentifierModel {

    private String configurationId;

    private String hostConfigurationId;

    public IdentifierModel(String configurationId, String hostConfigurationId) {
        this.configurationId = configurationId;
        this.hostConfigurationId = hostConfigurationId;
    }
}
