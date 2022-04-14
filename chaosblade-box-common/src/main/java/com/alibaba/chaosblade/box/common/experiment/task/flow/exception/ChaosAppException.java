package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;

/**
     * Exception which ChaosApp throws only
 *
 * @author sunju
 */
public class ChaosAppException extends RuntimeException {

    public ChaosAppException(String errorMsg) {
        super(errorMsg);
    }

    public ChaosAppException(Throwable e) {
        super(e);
    }

    public ChaosAppException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }

}
