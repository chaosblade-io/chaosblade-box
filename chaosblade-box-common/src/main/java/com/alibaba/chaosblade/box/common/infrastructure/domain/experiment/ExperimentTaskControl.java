package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author jiumu */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskControl {

  List<ResultEnum> resultStats;

  List<String> aoneApps;

  Map<String, String> appDescs;
}
