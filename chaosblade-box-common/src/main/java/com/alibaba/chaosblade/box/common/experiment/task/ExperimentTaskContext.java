package com.alibaba.chaosblade.box.common.experiment.task;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import java.util.Map;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentTaskContext {

  private Map<String, Object> miniAppContext;

  private ExperimentSchedulerConfig schedulerConfig;

  private boolean mockMode;

  private Integer source;

  private boolean includeJvm;

  /** 外部ID */
  private String outerId;

  /** 演练反馈重定向地址 */
  private String redirectUrl;
}
