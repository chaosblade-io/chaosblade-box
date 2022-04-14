package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import java.util.List;

/**
 * @author haibin.lhb
 * 
 * 
 */
public interface DefaultApplicationConfigurationLoader {

    /**
     * 加载默认的应用配置
     *
     * @return
     */
    public List<ApplicationConfiguration> loadConfigurations();
}
