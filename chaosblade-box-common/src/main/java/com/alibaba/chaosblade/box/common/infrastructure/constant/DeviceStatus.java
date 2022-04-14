package com.alibaba.chaosblade.box.common.infrastructure.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * @author changjun.xcj
 *
 */
public enum DeviceStatus {
    WAIT_INSTALL(0),
    INSTALLING(1),
    INSTALL_FAIL(-1),
    ONLINE(2),
    OFFLINE(3),
    UNINSTALLING(4),
    UNINSTALL_FAIL(5);

    private int status;

    DeviceStatus(int status) {
        this.status = status;
    }

    public static Set<Integer> getActivied() {
        Set<Integer> activated = new HashSet<>();
        activated.add(ONLINE.getStatus());
        return activated;
    }

    public static Set<Integer> getEnableInstallStatus() {
        Set<Integer> enableInstall = new HashSet<>();
        enableInstall.add(WAIT_INSTALL.getStatus());
        enableInstall.add(INSTALL_FAIL.getStatus());
        return enableInstall;
    }

    public static Set<Integer> getEnableUninstallStatus() {
        Set<Integer> enableInstall = new HashSet<>();
        enableInstall.add(ONLINE.getStatus());
        enableInstall.add(OFFLINE.getStatus());
        enableInstall.add(UNINSTALL_FAIL.getStatus());
        return enableInstall;
    }

    public int getStatus() {
        return status;
    }
}
