package com.alibaba.chaosblade.box.service.model.application;

import lombok.Data;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */

@Data
public class ApplicationGroupDeviceQuery {

    /**
     * 应用名
     */
    private String appName;

    /**
     * 应用分组名
     */
    private List<String> groupNames;

    /**
     * ip
     */
    private List<String> ips;

    private List<String> configurationIds;


}
