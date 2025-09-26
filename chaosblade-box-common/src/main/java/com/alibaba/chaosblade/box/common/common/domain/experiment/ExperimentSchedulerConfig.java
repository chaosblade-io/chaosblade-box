package com.alibaba.chaosblade.box.common.common.domain.experiment;

import java.util.Date;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentSchedulerConfig {

  /** 定时任务表达式 */
  private String cronExpression;

  /** 指定一次时间 */
  private Date fixedTime;
}
