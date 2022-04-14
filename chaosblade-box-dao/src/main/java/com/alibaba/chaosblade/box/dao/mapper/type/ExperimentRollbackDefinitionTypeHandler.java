package com.alibaba.chaosblade.box.dao.mapper.type;


import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentRollbackDefinition;

/**
 * @author haibin
 *
 *
 */
public class ExperimentRollbackDefinitionTypeHandler extends BaseFastJsonTypeHandler<ExperimentRollbackDefinition> {
    @Override
    public Class<ExperimentRollbackDefinition> getObjectClass() {
        return ExperimentRollbackDefinition.class;
    }
}
