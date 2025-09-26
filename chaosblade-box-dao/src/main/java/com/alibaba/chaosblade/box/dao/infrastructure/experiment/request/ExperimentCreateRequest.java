package com.alibaba.chaosblade.box.dao.infrastructure.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import java.util.HashMap;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentCreateRequest extends BaseExperimentRequest {

  private ExperimentDefinitionRequest definition;
  private HashMap<String, String> attributes;
  private String workspaceId;
}
