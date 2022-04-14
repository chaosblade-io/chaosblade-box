package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.chaosblade.box.dao.model.ApplicationConfigurationDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationConfigurationRepository;
import com.alibaba.chaosblade.box.service.model.application.ApplicationConfigurationUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class ApplicationConfigurator {

    @Autowired
    private ApplicationConfigurationProvider applicationConfigurationProvider;

    @Autowired
    private ApplicationConfigurationRepository applicationConfigurationRepository;

    public List<ApplicationConfiguration> provide(Long appId) {
        return applicationConfigurationProvider.provideApplicationConfigurations(appId);
    }

    public boolean updateOrCreateIfNotExistConfiguration(
        ApplicationConfigurationUpdateRequest applicationConfigurationUpdateRequest) {
        ApplicationConfigurationDO applicationConfigurationDO = applicationConfigurationRepository.findByAliasAndAppId(
            applicationConfigurationUpdateRequest.getAlias(), applicationConfigurationUpdateRequest.getAppId());
        if (applicationConfigurationDO == null) {
            applicationConfigurationDO = new ApplicationConfigurationDO();
            applicationConfigurationDO.setValue(applicationConfigurationUpdateRequest.getValue());
            applicationConfigurationDO.setAlias(applicationConfigurationUpdateRequest.getAlias());
            applicationConfigurationDO.setAppId(Long.valueOf(applicationConfigurationUpdateRequest.getAppId()));
            applicationConfigurationDO.setScope(applicationConfigurationUpdateRequest.getScope());
            applicationConfigurationDO.setOverride(applicationConfigurationUpdateRequest.getOverride());
            applicationConfigurationDO.setName(applicationConfigurationUpdateRequest.getName());
            return applicationConfigurationRepository.add(applicationConfigurationDO);
        }
        applicationConfigurationDO.setScope(applicationConfigurationUpdateRequest.getScope());
        applicationConfigurationDO.setValue(applicationConfigurationUpdateRequest.getValue());
        applicationConfigurationDO.setOverride(applicationConfigurationUpdateRequest.getOverride());
        return applicationConfigurationRepository.update(applicationConfigurationDO);
    }

}
