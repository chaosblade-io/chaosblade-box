package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinition;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentGuardArgument {

    /**
     * 全局恢复策略涉及到的小程序以及小程序配置
     */
    private List<SceneArgumentDefinition> arguments;

    /**
     * 容忍度,全局恢复策略触发的阈值
     */
    private List<RecoveryStrategyToleranceArgumentDefinition> tolerance;

    /**
     * 当选择小程序之后,需要选择某个类目下面具体某个值,比如选了CPU,那么fields就可能是system.cpu.util
     */
    private List<RecoveryStrategyFieldArgumentDefinition> fields;

}
