package com.alibaba.chaosblade.box.common.infrastructure.exception;

import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import lombok.Getter;

/**
 * @author haibin
 *
 * 
 */
@Getter
public class ChaosException extends RuntimeException {

    private IErrorCode errorCode;

    public ChaosException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ChaosException(IErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public ChaosException(ChaosError chaosError) {
        super(chaosError.getErrorMessage());
        this.errorCode = chaosError.getErrorCode();
    }

    public ChaosException(IErrorCode errorCode) {
        this(errorCode, errorCode.getReadableMessage());
    }

    public ChaosException(IErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}
