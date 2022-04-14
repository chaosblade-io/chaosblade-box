package com.alibaba.chaosblade.box.dao.infrastructure.checker.definition;

import com.alibaba.chaosblade.box.dao.infrastructure.model.ExperimentDefinitionContext;
import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainContext;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import lombok.Getter;

/**
 * @author haibin.lhb
 *
 *
 */
@Getter
public class MiniGroupCheckContext implements ChainContext {
	
	private final MiniFlowGroup miniFlowGroup;
	
	private final ExperimentDefinitionContext experimentDefinitionContext;
	
	public MiniGroupCheckContext(MiniFlowGroup miniFlowGroup,
		ExperimentDefinitionContext experimentDefinitionContext) {
		this.miniFlowGroup=miniFlowGroup;
		this.experimentDefinitionContext=experimentDefinitionContext;
	}
}
