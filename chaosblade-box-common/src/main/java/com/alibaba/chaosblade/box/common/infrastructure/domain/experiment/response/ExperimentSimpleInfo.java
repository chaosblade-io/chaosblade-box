package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import java.util.List;
import lombok.Data;

/** @author sunpeng */
@Data
public class ExperimentSimpleInfo {

  private String experimentId;

  private ExperimentBasicInfo basicInfo;

  private List<ExperimentFlowSimpleInfo> attack;
  private List<ExperimentFlowSimpleInfo> prepare;
  private List<ExperimentFlowSimpleInfo> check;
  private List<ExperimentFlowSimpleInfo> recover;
}
