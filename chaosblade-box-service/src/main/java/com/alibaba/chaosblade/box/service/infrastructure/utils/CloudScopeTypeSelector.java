package com.alibaba.chaosblade.box.service.infrastructure.utils;


import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.ScopeTypeSelector;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class CloudScopeTypeSelector implements ScopeTypeSelector {
	@Override
	public int selectByAppCode(String appCode) {
		return ScopeTypeEnum.judgeScopeByAppCode(appCode).getValue();
	}
}
