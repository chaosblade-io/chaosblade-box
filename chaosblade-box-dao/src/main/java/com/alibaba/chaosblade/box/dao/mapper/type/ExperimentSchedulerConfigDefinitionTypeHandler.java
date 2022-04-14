package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;

/**
 * @author haibin
 *
 *
 */
public class ExperimentSchedulerConfigDefinitionTypeHandler extends
    BaseFastJsonTypeHandler<ExperimentSchedulerConfig> {
    @Override
    public Class<ExperimentSchedulerConfig> getObjectClass() {
        return ExperimentSchedulerConfig.class;
    }
}
