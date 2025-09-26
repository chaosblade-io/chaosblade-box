package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentRunParam {

  private String outerId;

  private String empId;

  private List<ActivityRunParam> activities = new ArrayList<>();
}
