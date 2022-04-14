package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;


import com.alibaba.chaosblade.box.common.common.enums.IEnum;

/**
 * @author haibin
 *
 *
 */
public enum ExperimentGuardActionType implements IEnum<Integer> {

    OBSERVER(0),
    RECOVER(1);

    private int type;

    ExperimentGuardActionType(int type) {
        this.type = type;
    }

    @Override
    public Integer getValue() {
        return this.type;
    }
}
