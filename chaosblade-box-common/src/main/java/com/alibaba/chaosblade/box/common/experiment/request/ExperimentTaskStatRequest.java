package com.alibaba.chaosblade.box.common.experiment.request;

import lombok.Data;
import lombok.NonNull;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTaskStatRequest extends ExperimentTaskStatsRequest {

    @NonNull
    private String experimentId;

}
