package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentTaskGuardsResult {

  /** 观察指标 */
  private List<BaseExperimentGuardResultEntity> metrics;

  /** 恢复策略 */
  private List<ExperimentGuardRecoveryStrategyResultEntity> strategies;
}
