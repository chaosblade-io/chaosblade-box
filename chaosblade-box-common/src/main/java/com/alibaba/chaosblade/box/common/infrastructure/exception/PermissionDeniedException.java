package com.alibaba.chaosblade.box.common.infrastructure.exception;


import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;

/**
 * @author haibin
 *
 *
 */
public class PermissionDeniedException extends ChaosException {

    public PermissionDeniedException(IErrorCode errorCode) {
        super(errorCode);
    }

    public PermissionDeniedException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public PermissionDeniedException(IErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
