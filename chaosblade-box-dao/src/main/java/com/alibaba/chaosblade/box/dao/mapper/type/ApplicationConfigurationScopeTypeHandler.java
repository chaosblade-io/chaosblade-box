package com.alibaba.chaosblade.box.dao.mapper.type;


import com.alibaba.chaosblade.box.dao.infrastructure.configuration.ConfigurationScope;

/**
 * @author haibin.lhb
 *
 * 
 */
public class ApplicationConfigurationScopeTypeHandler extends BaseFastJsonTypeHandler<ConfigurationScope> {
    @Override
    public Class<ConfigurationScope> getObjectClass() {
        return ConfigurationScope.class;
    }
}
