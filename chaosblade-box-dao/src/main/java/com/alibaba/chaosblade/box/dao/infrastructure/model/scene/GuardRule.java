package com.alibaba.chaosblade.box.dao.infrastructure.model.scene;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinitionRepo;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Author: sunju
 *
 * Date:   2019/11/15
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuardRule {

    List<SceneFunctionParameterDO> parameters;

    List<RecoveryStrategyFieldArgumentDefinition> fields;

    List<RecoveryStrategyToleranceArgumentDefinition> tolerance;

    public void useDefaultTolerance() {
        tolerance = ImmutableList.of(
            RecoveryStrategyToleranceArgumentDefinitionRepo.TOLERANCE_HOST_PERCENT,
            RecoveryStrategyToleranceArgumentDefinitionRepo.TOLERANCE_DURATION
        );
    }

}
