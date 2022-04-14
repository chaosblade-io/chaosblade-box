package com.alibaba.chaosblade.box.common.infrastructure.exception;


import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;

/**
 * @author sunju
 *
 */
public class TagException extends ChaosException {

    public TagException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TagException(IErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
