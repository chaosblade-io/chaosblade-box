package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

/**
 * @author haibin
 *
 *
 */

public class WhereRuleItem {

    private String column;

    private String operator;

    private String value;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WhereRuleItem{");
        sb.append("column='").append(column).append('\'');
        sb.append(", operator='").append(operator).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
