package com.alibaba.chaosblade.box.common.infrastructure.constant;

/**
 * 应用的分组维度
 *
 * @author: xinyuan
 * @create: 2020-06-30 4:12 PM
 */

public enum ApplicationDimension {
    HOST(0),
    NODE(1),
    POD(2),
    PROCESS(3);

    private Integer value;

    ApplicationDimension(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
