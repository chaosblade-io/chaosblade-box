package com.alibaba.chaosblade.box.common.common.enums;

public enum MigrationStateEnum implements IEnum<Integer> {

    /**
     * 准备运行
     */
    READY(0),
    /**
     * 运行中
     */
    RUNNING(1),

    FAILED(2),
    /**
     * 已经结束
     */
    SUCCESS(3);

    private Integer value;

    MigrationStateEnum(Integer i) {
        this.value = i;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }


    public static MigrationStateEnum of(String state) {
        if (null == state || state.isEmpty()) {
            return null;
        }
        for (MigrationStateEnum status : MigrationStateEnum.values()) {
            if (status.name().equals(state.toUpperCase())) {
                return status;
            }
        }
        return null;
    }
}
