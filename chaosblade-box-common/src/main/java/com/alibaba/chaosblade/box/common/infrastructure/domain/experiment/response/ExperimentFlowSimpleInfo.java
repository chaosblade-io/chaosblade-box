package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response;

import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class ExperimentFlowSimpleInfo {

    private String name;

    private String appCode;

    public ExperimentFlowSimpleInfo(String appCode,String name) {
        this.name = name;
        this.appCode = appCode;
    }


}
