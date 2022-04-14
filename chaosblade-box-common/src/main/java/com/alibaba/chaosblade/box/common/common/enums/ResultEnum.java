package com.alibaba.chaosblade.box.common.common.enums;

/**
 * @author haibin
 *
 *
 */
public enum ResultEnum implements IEnum<Integer> {

    /**
     * 运行成功
     */
    SUCCESS(0),

    /**
     * 运行失败
     */
    FAILED(1),

    /**
     * 任务跳过
     */
    REJECTED(2),

    /**
     * 任务异常中断
     */
    ERROR(3),

    /**
     * 任务被终止
     */
    STOPPED(4),

    /**
     * 停止失败
     */
    SOPPED_FAILED(5);

    Integer value;

    ResultEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static ResultEnum of(Integer value) {
        for (ResultEnum state : ResultEnum.values()) {
            if (state.getValue().equals(value)) {
                return state;
            }
        }
        return null;
    }
}
