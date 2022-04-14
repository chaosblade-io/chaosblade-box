package com.alibaba.chaosblade.box.common.common.enums;

/**
 * @author haibin
 *
 *
 */
public enum ExperimentTaskTypeEnum implements IEnum<Integer> {

    /**
     * 手动演练，手动演练就是用户来控制演练周期以及里面的步骤执行
     */
    MANUAL(0),

    /**
     * 自动演练，就是演练流程自动化运行
     */
    AUTO(1);

    private int value;

    ExperimentTaskTypeEnum(int i) {
        this.value = i;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
