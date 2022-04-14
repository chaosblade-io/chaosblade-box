package com.alibaba.chaosblade.box.common.infrastructure.exception;

import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;

/**
 * Author: sunju
 *
 * Date:   2020/4/01
 */
public class InsufficientBalanceException extends ChaosException {

    public InsufficientBalanceException(IErrorCode errorCode) {
        super(errorCode);
    }

    public InsufficientBalanceException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InsufficientBalanceException(IErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
