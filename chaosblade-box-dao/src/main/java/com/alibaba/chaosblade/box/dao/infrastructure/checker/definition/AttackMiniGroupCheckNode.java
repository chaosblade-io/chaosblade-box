package com.alibaba.chaosblade.box.dao.infrastructure.checker.definition;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AttackMiniGroupCheckNode extends BaseMiniGroupCheckNode {
	@Override
	public Void invoke(MiniGroupCheckContext context) throws Exception {
		MiniFlowGroup miniFlowGroup = context.getMiniFlowGroup();
		List<MiniFlow> newMiniFlows = new ArrayList<>();
		for (MiniFlow miniFlow : miniFlowGroup.getFlows()) {
			if (!CollectionUtil.isNullOrEmpty(miniFlow.getAttack())) {
				newMiniFlows.add(miniFlow);
			} else {
				log.warn("mini flow not contains attack");
			}
		}
		miniFlowGroup.setFlows(newMiniFlows);
		return getNext().invoke(context);
	}
}
