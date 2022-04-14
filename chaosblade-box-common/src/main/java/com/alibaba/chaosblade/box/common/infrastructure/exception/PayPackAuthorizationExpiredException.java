package com.alibaba.chaosblade.box.common.infrastructure.exception;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;

/**
 * 用户资源包授权过期异常
 *
 * @author sunpeng
 *
 *
 */
public class  PayPackAuthorizationExpiredException extends ChaosException {

    public PayPackAuthorizationExpiredException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public PayPackAuthorizationExpiredException(IErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }

    public PayPackAuthorizationExpiredException(ChaosError ChaosError) {
        super(ChaosError);
    }

    public PayPackAuthorizationExpiredException(IErrorCode errorCode) {
        super(errorCode);
    }

    public PayPackAuthorizationExpiredException(IErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
