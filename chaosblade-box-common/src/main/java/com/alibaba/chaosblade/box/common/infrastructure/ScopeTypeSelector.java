package com.alibaba.chaosblade.box.common.infrastructure;

/**
 * @author haibin.lhb
 *
 *
 */

public interface ScopeTypeSelector {
	
	/**
	 * 根据appCode选择scope
	 *
	 * @param appCode 小程序code
	 * @return scope type
	 */
	public int selectByAppCode(String appCode);
}
