package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardRecoveryStrategyResultEntity;

/**
 * @author haibin
 *
 *
 */
public class ExperimentGuardRecoveryStrategyResultEntityTypeHandler extends BaseFastJsonTypeHandler<ExperimentGuardRecoveryStrategyResultEntity> {
    @Override
    public Class<ExperimentGuardRecoveryStrategyResultEntity> getObjectClass() {
        return ExperimentGuardRecoveryStrategyResultEntity.class;
    }
}
