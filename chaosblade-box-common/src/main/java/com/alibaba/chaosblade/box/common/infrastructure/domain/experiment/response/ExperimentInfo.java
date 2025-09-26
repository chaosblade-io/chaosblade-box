package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentInfo {

  private String experimentId;

  private ExperimentBasicInfo basicInfo;

  private ExperimentFlowInfo flowInfo;

  private List<ExperimentAppRisk> experimentAppRisks;

  private Integer permission;
}
