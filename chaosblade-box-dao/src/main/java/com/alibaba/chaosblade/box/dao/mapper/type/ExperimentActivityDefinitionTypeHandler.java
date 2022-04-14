package com.alibaba.chaosblade.box.dao.mapper.type;


import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;

/**
 * @author haibin
 *
 *
 */
public class ExperimentActivityDefinitionTypeHandler extends BaseFastJsonTypeHandler<ExperimentActivityDefinition> {

    @Override
    public Class<ExperimentActivityDefinition> getObjectClass() {
        return ExperimentActivityDefinition.class;
    }
}
