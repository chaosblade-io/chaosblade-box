package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;

/**
 * @author haibin
 * 
 * 
 */
public class ExperimentCreateException extends ChaosFlowException {

    @Override
    public ExceptionCode getExceptionCode() {
        return ExceptionCode.CREATE_EXPERIMENT_FAILED;
    }

    public ExperimentCreateException(String message) {
        super(message);
    }

    public ExperimentCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
