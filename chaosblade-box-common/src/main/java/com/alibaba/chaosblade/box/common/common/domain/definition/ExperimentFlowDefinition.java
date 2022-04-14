package com.alibaba.chaosblade.box.common.common.domain.definition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * 实验流程定义
 *
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentFlowDefinition {

    /**
     * 准备阶段
     */
    List<ExperimentActivityDefinition> prepare;

    /**
     * 检查阶段
     */
    List<ExperimentActivityDefinition> check;

    /**
     * 故障注入阶段
     */
    List<ExperimentActivityDefinition> attack;

    /**
     * 恢复阶段
     */
    List<ExperimentActivityDefinition> recover;

}
