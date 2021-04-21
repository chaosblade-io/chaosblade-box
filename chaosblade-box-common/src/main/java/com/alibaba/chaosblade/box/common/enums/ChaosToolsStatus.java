package com.alibaba.chaosblade.box.common.enums;

import java.util.Arrays;

/**
 * @author yefei
 */
public enum ChaosToolsStatus {

    UNINSTALLED((byte) 0),
    INSTALLED((byte) 1);

    private Byte code;

    ChaosToolsStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ChaosToolsStatus transByCode(Byte code) {
        return Arrays.stream(ChaosToolsStatus.values())
                .filter(o -> o.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
