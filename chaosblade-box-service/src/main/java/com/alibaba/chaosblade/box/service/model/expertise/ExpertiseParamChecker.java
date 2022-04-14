package com.alibaba.chaosblade.box.service.model.expertise;


import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 * 
 */
@Component
public class ExpertiseParamChecker {

    public ChaosError checkExpertiseBasicInfo(ExpertiseBasicInfo expertiseBasicInfo) {
        if (Strings.isNullOrEmpty(expertiseBasicInfo.getName())) {
            return ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL, "name Required");
        }
        if (Strings.isNullOrEmpty(expertiseBasicInfo.getFunctionDesc())) {
            return ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL, "functionDesc Required");
        }
        if (Strings.isNullOrEmpty(expertiseBasicInfo.getBackgroundDesc())) {
            return ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL, "backGroundDesc Required");
        }
        if (Strings.isNullOrEmpty(expertiseBasicInfo.getDesignConcept())) {
            return ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL, "designConcept Required");
        }
        return null;
    }

}
