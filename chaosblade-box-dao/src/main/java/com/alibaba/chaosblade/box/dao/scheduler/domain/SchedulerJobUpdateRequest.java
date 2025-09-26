package com.alibaba.chaosblade.box.dao.scheduler.domain;

import java.util.Date;
import lombok.Data;

/** @author haibin */
@Data
public class SchedulerJobUpdateRequest {

  public SchedulerJobUpdateRequest(Integer businessType, String businessId) {
    this.businessType = businessType;
    this.businessId = businessId;
  }

  private Integer businessType;

  private String businessId;

  private String cronExpression;

  private Date startTime;
}
