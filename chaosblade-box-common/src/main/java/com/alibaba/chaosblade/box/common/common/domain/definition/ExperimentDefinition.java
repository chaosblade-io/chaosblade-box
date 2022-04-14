package com.alibaba.chaosblade.box.common.common.domain.definition;

import com.alibaba.chaosblade.box.common.common.annotation.ApiParam;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 实验的定义，包含运行环境、执行方式、流程定义
 *
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentDefinition {

    /**
     * 演练流程定义
     */
    @ApiParam
    ExperimentFlowDefinition flow;

    /**
     * 回滚策略定义,可以为空
     */
    @Deprecated
    ExperimentRollbackDefinition rollback;
}
