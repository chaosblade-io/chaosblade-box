package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTaskGuardsResult {

    /**
     * 观察指标
     */
    private List<BaseExperimentGuardResultEntity> metrics;

    /**
     * 恢复策略
     */
    private List<ExperimentGuardRecoveryStrategyResultEntity> strategies;

}
