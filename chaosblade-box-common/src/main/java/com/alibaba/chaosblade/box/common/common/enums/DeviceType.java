package com.alibaba.chaosblade.box.common.common.enums;

import java.util.Arrays;

/**
 * @author: xinyuan
 * @create: 2018-03-27 下午5:30
 */
public enum DeviceType {
    HOST(0),
    CONTAINER(1),
    POD(2),
    HOST_POD(22);

    private int type;

    DeviceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static DeviceType transByCode(int code) {
        return Arrays.stream(DeviceType.values())
                .filter(o -> o.equals(code))
                .findFirst()
                .orElse(null);
    }
}
