package com.alibaba.chaosblade.box.common.app.sdk.constants;

import lombok.Getter;

/**
 * @author sunju
 *
 */
public enum PhaseType {

    /**
     * 阶段未定义
     */
    UNKNOWN(-1, -1),

    /**
     * 准备阶段
     */
    PREPARE(0, 1),

    ATTACK(1, 2),

    CHECK(2, 4),

    RECOVER(3, 8);

    @Getter
    private Integer type;

    @Getter
    private Integer compareFlag;

    PhaseType(int type, Integer compareFlag) {
        this.type = type;
        this.compareFlag = compareFlag;
    }

    public static PhaseType ofType(Integer type) {
        for (PhaseType phaseType : PhaseType.values()) {
            if (phaseType.type.equals(type)) {
                return phaseType;
            }
        }
        return UNKNOWN;
    }

    public static boolean isSupport(Integer source, Integer toSupport) {
        return (source & toSupport) == toSupport;
    }

}
