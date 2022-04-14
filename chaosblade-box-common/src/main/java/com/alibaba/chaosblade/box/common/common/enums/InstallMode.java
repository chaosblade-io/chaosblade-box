package com.alibaba.chaosblade.box.common.common.enums;

/**
 * Agent 安装模式，每一个namespace只能绑定一种安装模式；
 *
 */
public enum InstallMode {
	host,
	k8s,
	k8s_helm,
	cs_k8s,
	cs_k8s_helm;

	public static InstallMode getByName(String mode) {
		for (InstallMode item : InstallMode.values()) {
			if (item.name().equalsIgnoreCase(mode)) {
				return item;
			}
		}

		return null;
	}

	public static boolean isKubernetes(String mode) {
		if (mode != null && mode.contains("k8s")) {
			return true;
		}

		return false;
	}

}
