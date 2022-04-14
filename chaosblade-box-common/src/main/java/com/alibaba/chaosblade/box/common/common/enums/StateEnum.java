package com.alibaba.chaosblade.box.common.common.enums;

/**
 * @author haibin
 *
 *
 */
public enum StateEnum implements IEnum<Integer> {

    /**
     * 准备运行
     */
    READY(0),
    /**
     * 运行中
     */
    RUNNING(1),

    /**
     * 暂停
     */
    SUSPEND(2),
    /**
     * 停止当中
     */
    STOPPING(3),

    /**
     * 已经结束
     */
    FINISHED(4);

    private Integer value;

    StateEnum(Integer value) {
        this.value = value;

    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
