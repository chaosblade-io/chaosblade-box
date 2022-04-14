package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.HashMapSettings;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.InitMiniFlowRequest;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author haibin.lhb
 *
 * 
 */
@Data
public class InitMiniFlowByAppCodeInterceptorContext extends HashMapSettings {

    InitMiniFlowRequest initMiniFlowRequest;

    String appCode;

    Map<String, List<ExperimentActivityInfo>> response;

}
