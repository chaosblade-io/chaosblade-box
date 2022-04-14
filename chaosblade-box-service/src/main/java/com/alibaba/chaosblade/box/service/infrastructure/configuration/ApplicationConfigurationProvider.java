package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface ApplicationConfigurationProvider {

    /**
     * 获取应用默认配置
     *
     * @param appId 应用Id
     * @return
     */
    public List<ApplicationConfiguration> provideApplicationConfigurations(Long appId);

}
