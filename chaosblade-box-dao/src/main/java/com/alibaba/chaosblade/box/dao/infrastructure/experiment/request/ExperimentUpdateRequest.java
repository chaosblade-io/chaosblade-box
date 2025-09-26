package com.alibaba.chaosblade.box.dao.infrastructure.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import java.io.Serializable;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentUpdateRequest extends BaseExperimentRequest implements Serializable {

  private String experimentId;

  private ExperimentDefinitionRequest definition;
}
