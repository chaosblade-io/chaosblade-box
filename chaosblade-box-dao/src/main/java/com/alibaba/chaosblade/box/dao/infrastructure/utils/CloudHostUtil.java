package com.alibaba.chaosblade.box.dao.infrastructure.utils;



import com.alibaba.chaosblade.box.common.common.enums.InstallMode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author haibin
 *
 *
 */
public final class CloudHostUtil {

    private static Set<String> K8sInstallModes = new HashSet<>();

    static {
        K8sInstallModes.add(InstallMode.cs_k8s.name());
        K8sInstallModes.add(InstallMode.cs_k8s_helm.name());
        K8sInstallModes.add(InstallMode.k8s.name());
        K8sInstallModes.add(InstallMode.k8s_helm.name());
    }

    public static boolean isK8sInstallMode(String installMode) {
        return K8sInstallModes.contains(installMode);
    }
}
