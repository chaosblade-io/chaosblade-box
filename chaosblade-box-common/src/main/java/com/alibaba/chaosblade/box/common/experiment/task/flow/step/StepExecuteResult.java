package com.alibaba.chaosblade.box.common.experiment.task.flow.step;

import java.io.Serializable;
import lombok.Data;

/** @author haibin */
@Data
public class StepExecuteResult implements Serializable {

  protected boolean success;

  private Throwable error;
}
