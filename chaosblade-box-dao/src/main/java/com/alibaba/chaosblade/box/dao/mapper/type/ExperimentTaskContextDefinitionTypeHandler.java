package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;

/**
 * @author haibin
 *
 *
 */
public class ExperimentTaskContextDefinitionTypeHandler extends BaseFastJsonTypeHandler<ExperimentTaskContext> {
    @Override
    public Class<ExperimentTaskContext> getObjectClass() {
        return ExperimentTaskContext.class;
    }
}
