package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;

/**
 * @author haibin
 *
 *
 */
public class ActionNotAllowedException extends ChaosFlowException {
    public ActionNotAllowedException(String message) {
        super(message);
    }

    @Override
    public ExceptionCode getExceptionCode() {
        return ExceptionCode.ACTION_NOT_ALLOW;
    }
}
