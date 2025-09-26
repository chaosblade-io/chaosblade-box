package com.alibaba.chaosblade.box.common.experiment.clientobject;

import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskResult {

  Integer totalCount;

  Integer successCount;

  Integer stoppedCount;

  Integer failedCount;

  Integer errorCount;

  Set<String> aoneApps;

  Set<String> machines;

  Set<String> appDescs;
}
