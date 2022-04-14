package com.alibaba.chaosblade.box.common.common.domain.definition;

import com.alibaba.chaosblade.box.common.common.annotation.ApiParam;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 实验流程节点定义
 *
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentActivityDefinition extends BaseExperimentActivityDefinition {

    /**
     * 活动的参数
     */
    @ApiParam(required = false)
    ExperimentNodeArgumentsDefinition arguments;

}
