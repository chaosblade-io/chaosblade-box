package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow;

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class MiniFlowDefinition {

    /**
     * 微流程准备环节
     */
    private List<ExperimentActivityDefinition> prepare;

    /**
     * 微流程注入环节
     */
    private ExperimentActivityDefinition attack;

    /**
     * 微流程校验环节
     */
    private List<ExperimentActivityDefinition> check;

    /**
     * 微流程恢复环节
     */
    private List<ExperimentActivityDefinition> recover;
}
