package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentGuardConfiguration {

  private List<ExperimentGuard> guards = new ArrayList<>();
}
