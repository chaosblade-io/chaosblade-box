package com.alibaba.chaosblade.box.service.model.overview;

import java.util.Date;
import lombok.Data;

/** @author sunpeng */
@Data
public class OverviewExperimentTaskDayCount {

  private Date time;

  private Integer totalCount;

  private Integer timedTaskCount;
}
