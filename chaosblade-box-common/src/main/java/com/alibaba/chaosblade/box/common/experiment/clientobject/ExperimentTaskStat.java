package com.alibaba.chaosblade.box.common.experiment.clientobject;

import lombok.Data;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ExperimentTaskStat {

    private String experimentId;

    private String experimentName;

    private Integer totalCount;

    private Integer runningCount;

    private Integer successCount;

    private Integer failureCount;

    private Integer exceptionCount;

    /**
     * 平均用时
     */
    private long averageCostInSecond;

}
