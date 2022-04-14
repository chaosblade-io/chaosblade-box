package com.alibaba.chaosblade.box.service.model.param;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ParamOptionsQueryRequest extends BaseRequest {
	
	/**
	 * 所有机器列表
	 */
	private List<Host> hosts;
	
	/**
	 * 参数名
	 */
	private String alias;
	
	/**
	 * 当前选择的演练场景
	 */
	private String appCode;
	
	/**
	 * 应用Id
	 */
	private String appId;

	/**
	 * 机器id列表
	 */
	private List<String> configurationIds;
	
}
