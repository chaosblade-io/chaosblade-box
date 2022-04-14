package com.alibaba.chaosblade.box.common.common.enums;

/**
 * @author haibin
 *
 *
 */
public enum UserCheckState implements IEnum<Integer> {

    /**
     * 等待用户确认
     */
    USER_CHECK_STATE_WAITING(0),

    /**
     * 用户确认通过
     */
    USER_CHECK_STATE_PASSED(1),

    /**
     * 用户确认失败
     */
    USER_CHECK_STATE_FAILED(2);

    private Integer type;

    UserCheckState(Integer type) {
        this.type = type;
    }

    @Override
    public Integer getValue() {
        return this.type;
    }
}
