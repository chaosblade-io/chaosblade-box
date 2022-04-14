package com.alibaba.chaosblade.box.common.common.domain.task;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class BaseExperimentTaskRequest extends BaseRequest {

    private String taskId;
}
