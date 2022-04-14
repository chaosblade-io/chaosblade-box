package com.alibaba.chaosblade.box.common.common.domain.definition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 实验流程节点间隔定义，单位：毫秒
 *
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentNodePausesDefinition {

    long before;

    long after;

}
