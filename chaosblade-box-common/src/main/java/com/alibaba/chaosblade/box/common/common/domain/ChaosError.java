package com.alibaba.chaosblade.box.common.common.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haibin
 *
 *
 */
@Getter
@Setter
public class ChaosError {

    private IErrorCode errorCode;

    private String code;

    private String errorMessage;

    private Integer codeStatus;

    public ChaosError(IErrorCode code, String errorMessage) {
        this.errorCode = code;
        this.code = code.name();
        this.errorMessage = errorMessage;
        this.codeStatus = code.status();
    }

    public ChaosError(IErrorCode code) {
        this(code, code.getReadableMessage());
    }

    public static ChaosError withCode(IErrorCode errorCode) {
        return new ChaosError(errorCode);
    }

    public static ChaosError withCode(IErrorCode errorCode, String errorMessage) {
        return new ChaosError(errorCode, errorMessage);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ChaosError{");
        sb.append("code=").append(code);
        sb.append(", message='").append(errorMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
