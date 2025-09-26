package com.alibaba.chaosblade.box.service.model.overview;

import java.util.Date;
import java.util.List;
import lombok.Data;

/** @author sunpeng */
@Data
public class OverviewExperimentTask {

  private Date lastExperimentTaskTime;

  private List<OverviewExperimentTaskDayCount> experimentTaskDayCountList;
}
