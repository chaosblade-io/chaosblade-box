package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinition;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentGuard {

    private String guardId;

    private String name;

    private String appCode;

    private Integer actionType;

    private boolean required;

    /**
     * mini app args
     */
    private List<SceneArgumentDefinition> arguments;

    private List<RecoveryStrategyToleranceArgumentDefinition> tolerance;

    private List<RecoveryStrategyFieldArgumentDefinition> fields;

}
