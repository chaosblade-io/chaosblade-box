package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;

/**
 * @author haibin
 *
 *
 */
public abstract class ChaosFlowException extends RuntimeException {

    public abstract ExceptionCode getExceptionCode();

    public ChaosFlowException(String message) {
        super(message);
    }

    public ChaosFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return ExceptionHelper.buildMessage(super.getMessage(), getCause());
    }

    /**
     * Retrieve the innermost cause of this exception, if any.
     *
     * @return the innermost exception, or {@code null} if none
     * @since 2.0
     */
    public Throwable getRootCause() {
        return ExceptionHelper.getRootCause(this);
    }

    public Throwable getThrowable() {
        Throwable throwable = getRootCause();
        return throwable == null ? this : throwable;
    }

}
