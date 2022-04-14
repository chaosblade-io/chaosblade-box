package com.alibaba.chaosblade.box.common.common.enums;

/**
 * @author haibin
 *
 *
 */
public enum AppType {

    HOST(0),

    CLUSTER(1);

    public int getType() {
        return type;
    }

    private int type;

    AppType(int type) {
        this.type = type;
    }

    public static AppType judeAppTypeByDeviceType(DeviceType deviceType) {
        if (DeviceType.HOST.equals(deviceType)) {
            return AppType.HOST;
        } else {
            return AppType.CLUSTER;
        }
    }
}
