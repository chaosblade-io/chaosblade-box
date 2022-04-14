package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

import java.util.Date;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class OverviewExperimentTaskDayCount {

    private Date time;

    private Integer totalCount;

    private Integer timedTaskCount;

}
