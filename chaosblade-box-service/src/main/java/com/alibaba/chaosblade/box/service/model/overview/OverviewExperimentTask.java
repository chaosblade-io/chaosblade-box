package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class OverviewExperimentTask {

    private Date lastExperimentTaskTime;

    private List<OverviewExperimentTaskDayCount> experimentTaskDayCountList;

}
