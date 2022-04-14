package com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;

/**
 * @author haibin.lhb
 *
 * 
 */
public class ActivityDefinitionIllegalException extends ChaosException {

    public ActivityDefinitionIllegalException(
        String message) {
        super(CommonErrorCode.P_EXPERIMENT_DEFINITION_ILLEGAL, message);
    }

    public ActivityDefinitionIllegalException(IErrorCode errorCode,
                                              String message) {
        super(errorCode, message);
    }

    public ActivityDefinitionIllegalException(IErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }

    public ActivityDefinitionIllegalException(ChaosError chaosError) {
        super(chaosError);
    }

    public ActivityDefinitionIllegalException(IErrorCode errorCode) {
        super(errorCode);
    }

    public ActivityDefinitionIllegalException(IErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
