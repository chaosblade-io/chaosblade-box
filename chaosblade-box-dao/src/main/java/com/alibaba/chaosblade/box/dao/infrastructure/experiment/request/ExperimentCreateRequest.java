package com.alibaba.chaosblade.box.dao.infrastructure.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import lombok.Data;

import java.util.HashMap;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentCreateRequest extends BaseExperimentRequest {

    private ExperimentDefinitionRequest definition;
    private HashMap<String, String> attributes;
    private String workspaceId;

}
