package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;

/**
 * @author haibin
 *
 *
 */
public class ActivityExecuteFailedException extends ChaosFlowException {
    @Override
    public ExceptionCode getExceptionCode() {
        return ExceptionCode.ACTIVITY_EXECUTE_FAILED;
    }

    public ActivityExecuteFailedException(String message) {
        super(message);
    }

    public ActivityExecuteFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
