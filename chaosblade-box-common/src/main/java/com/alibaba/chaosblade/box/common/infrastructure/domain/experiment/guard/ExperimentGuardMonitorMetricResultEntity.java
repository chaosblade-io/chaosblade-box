package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import java.io.Serializable;
import lombok.Data;

@Data
public class ExperimentGuardMonitorMetricResultEntity extends BaseExperimentGuardResultEntity
    implements Serializable {

  private String unit;
}
