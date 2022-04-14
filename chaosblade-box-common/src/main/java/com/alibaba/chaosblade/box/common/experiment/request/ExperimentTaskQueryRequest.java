package com.alibaba.chaosblade.box.common.experiment.request;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTaskQueryRequest {

    public ExperimentTaskQueryRequest(String taskId) {
        this.taskId = taskId;
    }

    private String taskId;
}
