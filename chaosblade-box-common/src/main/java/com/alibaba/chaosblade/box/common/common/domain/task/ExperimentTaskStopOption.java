package com.alibaba.chaosblade.box.common.common.domain.task;

import java.io.Serializable;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentTaskStopOption implements Serializable {

  /** 是否同步返回停止结果 */
  private boolean sync;

  private String experimentId;
}
