package com.alibaba.chaosblade.box.common.infrastructure.error;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
public class ThrowableChaosErrorWrappers {

    @Autowired
    private List<ThrowableChaosErrorWrapper> wrapperList;

    public ChaosError wrapper(Throwable throwable, ChaosError defaultChaosError) {
        ChaosError chaosError = null;
        for (ThrowableChaosErrorWrapper throwableChaosErrorWrapper : wrapperList) {
            chaosError = throwableChaosErrorWrapper.wrapper(throwable);
            if (chaosError != null) {
                break;
            }
        }
        if (chaosError == null) {
            chaosError = defaultChaosError;
        }
        return chaosError;
    }

    public ChaosError wrapper(Throwable throwable, IErrorCode iErrorCode) {
        ChaosError chaosError = null;
        for (ThrowableChaosErrorWrapper throwableChaosErrorWrapper : wrapperList) {
            chaosError = throwableChaosErrorWrapper.wrapper(throwable);
            if (chaosError != null) {
                break;
            }
        }
        if (chaosError == null) {
            chaosError = new ChaosError(iErrorCode, ExceptionHelper.detailedMessage(throwable));
        }
        return chaosError;
    }

    public ChaosError wrapper(Throwable throwable, String message) {
        ChaosError chaosError = wrapper(throwable, new ChaosError(CommonErrorCode.S_SYSTEM_ERROR, message));
        if (!chaosError.getErrorCode().equals(CommonErrorCode.S_SYSTEM_ERROR)) {
            chaosError.setErrorMessage(message);
        }
        return chaosError;
    }

    @PostConstruct
    public void init() {
        wrapperList.sort(AnnotationAwareOrderComparator.INSTANCE);
    }

}
