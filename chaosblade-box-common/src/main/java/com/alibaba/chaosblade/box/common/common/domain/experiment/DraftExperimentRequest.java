package com.alibaba.chaosblade.box.common.common.domain.experiment;

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentFlowDefinition;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DraftExperimentRequest extends BaseExperimentRequest {

    ExperimentFlowDefinition flow;

}
