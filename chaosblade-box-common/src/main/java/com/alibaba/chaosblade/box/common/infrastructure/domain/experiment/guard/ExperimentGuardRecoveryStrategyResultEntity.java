package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinition;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentGuardRecoveryStrategyResultEntity extends BaseExperimentGuardResultEntity {

  private List<RecoveryStrategyToleranceArgumentDefinition> tolerance;

  private List<RecoveryStrategyFieldArgumentDefinition> fields;

  private String strategyDesc;
}
