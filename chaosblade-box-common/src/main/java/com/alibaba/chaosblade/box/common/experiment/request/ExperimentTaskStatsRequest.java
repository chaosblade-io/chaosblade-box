package com.alibaba.chaosblade.box.common.experiment.request;

import java.util.Date;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentTaskStatsRequest {

  private Date startTime;
  private Date endTime;
}
