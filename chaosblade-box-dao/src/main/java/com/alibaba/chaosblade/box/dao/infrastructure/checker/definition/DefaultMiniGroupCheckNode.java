package com.alibaba.chaosblade.box.dao.infrastructure.checker.definition;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainRootNode;
import com.alibaba.chaosblade.box.common.infrastructure.constant.HostSelectTypes;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@ChainRootNode
public class DefaultMiniGroupCheckNode extends BaseMiniGroupCheckNode {

	@Override
	public Void invoke(MiniGroupCheckContext context) throws Exception {
		List<Host> hosts = context.getMiniFlowGroup().getHosts();
		checkHost(hosts, context.getMiniFlowGroup().getSelectType());
		return null;
	}

	public void checkHost(List<Host> hosts, Integer selectType) {
		if (HostSelectTypes.SELECT_TYPE_IP.equals(selectType)) {
			if (CollectionUtil.isNullOrEmpty(hosts)) {
				throw new ChaosException(CommonErrorCode.P_HOST_REQUIRED);
			}
		}
	}
}
