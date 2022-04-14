package com.alibaba.chaosblade.box.common.infrastructure.enums;


import com.alibaba.chaosblade.box.common.common.enums.IEnum;

public enum ActivityTaskMode implements IEnum<Integer> {

    /**
     * 活动级别
     */
    ACTIVITY(0),

    /**
     * 实验级别
     */
    EXPERIMENT(2);

    private int type;

    ActivityTaskMode(int i) {
        this.type = i;
    }

    @Override
    public Integer getValue() {
        return this.type;
    }}
