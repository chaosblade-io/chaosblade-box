package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import java.io.Serializable;
import lombok.Data;

/** @author haibin */
@Data
public class GuardCondition implements Serializable {

  /** 值类型 */
  private String type;

  /** 列名 */
  private String filedName;

  /** opration */
  private ConditionOperation operation;

  /** 值 */
  private String value;

  /** and操作 */
  private boolean and;
}
