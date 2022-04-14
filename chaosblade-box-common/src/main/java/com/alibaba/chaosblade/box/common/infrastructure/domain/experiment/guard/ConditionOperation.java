package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

/**
 * @author haibin
 *
 *
 */
public enum ConditionOperation {

    Include("包含"),
    GE("大于等于"),
    EQ("等于"),
    LE("小于等于"),
    GT("大于"),
    LT("小于"),
    DU("持续"),
    TIME("次数");

    private String desc;

    ConditionOperation(String desc) {
        this.desc = desc;
    }
}
