package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import lombok.AllArgsConstructor;

/**
 * @author haibin
 *
 *
 */
@AllArgsConstructor
public class ExperimentOperator {

    public String getOperatorId() {
        return operatorId;
    }

    private String operatorId;

    private ExperimentOperatorType operatorType;

    public ExperimentOperatorType getOperatorType() {
        if (this.operatorType == null) {
            return ExperimentOperatorType.API;
        }
        return this.operatorType;
    }

    public boolean isApi() {
        return ExperimentOperatorType.API.equals(operatorType);
    }

}
