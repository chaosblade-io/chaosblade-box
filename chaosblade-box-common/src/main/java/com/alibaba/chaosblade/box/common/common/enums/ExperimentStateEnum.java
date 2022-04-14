package com.alibaba.chaosblade.box.common.common.enums;

/**
 * 演练的状态,
 * <p>
 * FROZEN->READY
 * </p>
 *
 * @author haibin
 *
 *
 */
public enum ExperimentStateEnum implements IEnum<Integer> {

    /**
     * 不可见
     */
    INVISIBLE(-2),

    DRAFT(-1),
    /**
     * 准备好可以使用
     */
    READY(0),
    /**
     * 当前演练有一个任务正在运行当中
     */
    RUNNING(1),

    FINISHED(5);

    Integer state;

    ExperimentStateEnum(int state) {
        this.state = state;
    }

    @Override
    public Integer getValue() {
        return this.state;
    }

    public static Integer getValue(ExperimentStateEnum state) {
        if (null == state) {
            return null;
        }
        return state.getValue();
    }
}
