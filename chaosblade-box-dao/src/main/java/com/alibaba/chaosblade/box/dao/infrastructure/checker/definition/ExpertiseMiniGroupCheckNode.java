package com.alibaba.chaosblade.box.dao.infrastructure.checker.definition;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
@Order(100)
public class ExpertiseMiniGroupCheckNode extends BaseMiniGroupCheckNode {
	@Override
	public Void invoke(MiniGroupCheckContext context) throws Exception {
		if (context.getExperimentDefinitionContext().getExperimentDefinitionRequest().isExpertise()) {
			return null;
		}
		return getNext().invoke(context);
	}
}
