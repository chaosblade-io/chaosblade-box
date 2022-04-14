package com.alibaba.chaosblade.box.common.common.domain.feedback;

import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTaskRequest;
import lombok.Data;

/**
 * @author haibin.lhb
 *
 * 
 */
@Data
public class ExperimentTaskFeedbackSubmitRequest extends BaseExperimentTaskRequest {

    private ExperimentTaskFeedback feedback;

}
