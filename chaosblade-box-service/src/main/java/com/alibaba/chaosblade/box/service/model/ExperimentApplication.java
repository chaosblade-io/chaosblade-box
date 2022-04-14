package com.alibaba.chaosblade.box.service.model;

import lombok.Data;

/**
 * @author haibin.lhb
 * 
 *
 */
@Data
public class ExperimentApplication {

    private String appId;

    /**
     * 运行中的演练梳理
     */
    private Long taskCount;

    private String appName;

}
