package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

/**
 * @author jiumu
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskControl {

    List<ResultEnum> resultStats;

    List<String> aoneApps;

    Map<String, String> appDescs;
}
