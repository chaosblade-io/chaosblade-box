package com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard;

import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 * 
 *
 */
@Data
public class ExperimentGuardInstanceExecutionRequest {

    private ExperimentTaskDO experimentTaskDO;

    List<ExperimentGuardInstanceDO> experimentGuardInstances;

    private boolean fromScheduler;

    public ExperimentGuardInstanceExecutionRequest(
        ExperimentTaskDO experimentTaskDO,
        List<ExperimentGuardInstanceDO> experimentGuardInstances, boolean fromScheduler) {
        this.experimentTaskDO = experimentTaskDO;
        this.experimentGuardInstances = experimentGuardInstances;
        this.fromScheduler = fromScheduler;
    }
}
