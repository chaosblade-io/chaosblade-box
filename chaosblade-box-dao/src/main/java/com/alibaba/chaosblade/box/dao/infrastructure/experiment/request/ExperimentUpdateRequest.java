package com.alibaba.chaosblade.box.dao.infrastructure.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentUpdateRequest extends BaseExperimentRequest implements Serializable {

    private String experimentId;

    private ExperimentDefinitionRequest definition;

}
