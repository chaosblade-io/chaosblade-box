package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class ExperimentInit {

    private String scopeType;

    private Map<String, List<ExperimentActivityInfo>> flows;

}
