package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;


import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ExperimentFlowDefinitionMeta;

/**
 * 保存前的definition处理
 *
 * @author haibin
 *
 *
 */
public interface ExperimentActivityDefinitionInterceptor {


    /**
     * 处理activity definition
     *
     * @param originDefinition             原始的定义
     * @param phaseType                    阶段类型
     * @param experimentFlowDefinitionMeta experimentFlowDefintionMetaData
     */
    public void preInterceptor(ExperimentActivityDefinition originDefinition,
                               PhaseType phaseType, ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta);
}
