package com.alibaba.chaosblade.box.common.common.enums;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
public enum ScopeTypeEnum implements IEnum<Integer> {
	
	/**
	 * host安装类型，包括了ecs和用户自己的主机
	 */
	HOST(0, InstallMode.host),
	K8s(2, InstallMode.k8s, InstallMode.k8s_helm, InstallMode.cs_k8s, InstallMode.cs_k8s_helm);
	
	private InstallMode[] installModes;
	
	private int type;
	
	ScopeTypeEnum(Integer scopeType, InstallMode... installModes) {
		this.type = scopeType;
		this.installModes = installModes;
	}
	
	public static ScopeTypeEnum judgeScopeByAppType(Integer appType) {
		if (AppType.CLUSTER.getType() == appType) {
			return K8s;
		}
		if (AppType.HOST.getType() == appType) {
			return HOST;
		}
		return null;
	}
	
	public static ScopeTypeEnum judgeScopeByAppCode(String appCode) {
		if (MiniAppUtils.isK8S(appCode)) {
			return K8s;
		}
		return HOST;
	}
	
	public List<InstallMode> getAsList() {
		return Collections.unmodifiableList(Arrays.asList(installModes));
	}
	
	@Override
	public Integer getValue() {
		return type;
	}
}
