package com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition;


import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;

/**
 * @author haibin
 *
 * 
 */
public interface ActivityDefinitionChecker {

    /**
     * 校验节点定义
     *
     * @param experimentActivityDefinition 演练定义
     * @param fromApi                      是否来自api
     * @throws ActivityDefinitionIllegalException
     */
    public void check(ExperimentActivityDefinition experimentActivityDefinition, boolean fromApi)
        throws ActivityDefinitionIllegalException;
}
