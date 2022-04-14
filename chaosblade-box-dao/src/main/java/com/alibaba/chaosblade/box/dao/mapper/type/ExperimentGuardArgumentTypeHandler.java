package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardArgument;

/**
 * @author haibin
 * 
 *
 */
public class ExperimentGuardArgumentTypeHandler extends BaseFastJsonTypeHandler<ExperimentGuardArgument> {
    @Override
    public Class<ExperimentGuardArgument> getObjectClass() {
        return ExperimentGuardArgument.class;
    }
}
