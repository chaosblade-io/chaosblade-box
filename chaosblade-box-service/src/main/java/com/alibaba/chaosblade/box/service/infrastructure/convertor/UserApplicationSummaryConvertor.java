package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.service.model.application.UserApplicationSummary;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class UserApplicationSummaryConvertor extends BaseApplicationConvertor<ApplicationDO, UserApplicationSummary> {

    @Override
    public UserApplicationSummary convert(ApplicationDO applicationDO) {
        UserApplicationSummary userApplicationSummary = new UserApplicationSummary();
        userApplicationSummary.setAppId(String.valueOf(applicationDO.getId()));
        userApplicationSummary.setAppType(applicationDO.getAppType());
        userApplicationSummary.setAppName(applicationDO.getAppName());
        userApplicationSummary.setIsDefault(false);
        userApplicationSummary.setMachineCount(getMachineCount(applicationDO.getId()));
        userApplicationSummary.setExperimentTaskCount(getExperimentTaskCount(applicationDO.getId()));
        return userApplicationSummary;
    }

}
