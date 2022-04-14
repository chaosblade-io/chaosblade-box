package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTaskRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTaskStopRequest extends BaseExperimentTaskRequest {

    private boolean sync;

    private boolean fromScheduler;
}
