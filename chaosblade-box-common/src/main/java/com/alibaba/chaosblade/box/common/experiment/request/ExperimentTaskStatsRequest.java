package com.alibaba.chaosblade.box.common.experiment.request;

import lombok.Data;

import java.util.Date;

/**
 * @author haibin
 * 
 *
 */
@Data
public class ExperimentTaskStatsRequest {

    private Date startTime;
    private Date endTime;
}
