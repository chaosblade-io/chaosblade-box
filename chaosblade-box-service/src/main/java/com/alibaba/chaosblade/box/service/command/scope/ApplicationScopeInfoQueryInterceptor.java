package com.alibaba.chaosblade.box.service.command.scope;

import com.alibaba.chaosblade.box.dao.infrastructure.app.AppInfo;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.service.model.scope.ScopeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class ApplicationScopeInfoQueryInterceptor implements ScopeInfoQueryInterceptor {

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Override
    public void doQuery(ScopeInfo scopeInfo, DeviceDO deviceDO) {
        applicationDeviceRepository.findById(deviceDO.getConfigurationId()).ifPresent(
            applicationDeviceDO -> {
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(applicationDeviceDO.getAppName());
                appInfo.setAppId(applicationDeviceDO.getAppId());
                scopeInfo.setAppInfo(appInfo);
            });
    }
}
