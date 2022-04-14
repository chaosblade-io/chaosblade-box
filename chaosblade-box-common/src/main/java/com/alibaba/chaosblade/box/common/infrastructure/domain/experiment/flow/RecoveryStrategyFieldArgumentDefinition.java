package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ConditionArgumentDefinitionOperation;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecoveryStrategyFieldArgumentDefinition extends SceneArgumentDefinition {

    boolean and = true;

    ConditionArgumentDefinitionOperation operation;

    List<ConditionArgumentDefinitionOperation> operations;
}
