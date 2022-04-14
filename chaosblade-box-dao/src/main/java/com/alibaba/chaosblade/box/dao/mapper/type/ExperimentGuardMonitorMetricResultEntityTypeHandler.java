package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;

/**
 * @author haibin
 *
 *
 */
public class ExperimentGuardMonitorMetricResultEntityTypeHandler
    extends BaseFastJsonTypeHandler<ExperimentGuardMonitorMetricResultEntity> {
    @Override
    public Class<ExperimentGuardMonitorMetricResultEntity> getObjectClass() {
        return ExperimentGuardMonitorMetricResultEntity.class;
    }
}
