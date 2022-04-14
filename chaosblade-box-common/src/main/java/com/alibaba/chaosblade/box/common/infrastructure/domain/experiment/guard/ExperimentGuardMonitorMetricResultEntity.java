package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import lombok.Data;

import java.io.Serializable;


@Data
public class ExperimentGuardMonitorMetricResultEntity extends BaseExperimentGuardResultEntity implements Serializable {

    private String unit;
}
