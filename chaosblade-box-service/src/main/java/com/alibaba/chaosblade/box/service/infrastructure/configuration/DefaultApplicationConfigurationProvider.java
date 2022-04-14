package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.chaosblade.box.dao.infrastructure.configuration.ConfigurationScope;
import com.alibaba.chaosblade.box.dao.model.ApplicationConfigurationDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationConfigurationRepository;
import com.alibaba.chaosblade.box.dao.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 * 
 */
@Slf4j
@Component
public class DefaultApplicationConfigurationProvider implements ApplicationConfigurationProvider {

    @Autowired
    private DefaultApplicationConfigurationLoader defaultApplicationConfigurationLoader;

    @Autowired
    private ApplicationConfigurationRepository applicationConfigurationRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public List<ApplicationConfiguration> provideApplicationConfigurations(Long appId) {
        ApplicationDO applicationDO = applicationRepository.findById(appId).orElse(null);
        if (applicationDO == null) {
            return new ArrayList<>();
        }
        List<ApplicationConfiguration> defaultApplicationConfigurations = defaultApplicationConfigurationLoader
                .loadConfigurations();
        List<ApplicationConfiguration> applicationConfigurations = applicationConfigurationRepository
                .findByApplicationId(appId).stream().map(
                        this::convertApplicationConfiguration).collect(
                        Collectors.toList());
        return mergeConfigurations(applicationConfigurations, defaultApplicationConfigurations).stream().filter(
                applicationConfiguration -> applicationConfiguration.getAppType() == null
                        || applicationConfiguration.getAppType().equals(applicationDO.getAppType())).collect(
                Collectors.toList());
    }

    private ApplicationConfiguration convertApplicationConfiguration(
            ApplicationConfigurationDO applicationConfigurationDO) {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        applicationConfiguration.setAlias(applicationConfigurationDO.getAlias());
        applicationConfiguration.setAppId(String.valueOf(applicationConfigurationDO.getAppId()));
        applicationConfiguration.setScope(applicationConfigurationDO.getScope());
        applicationConfiguration.setStatus(ApplicationConfiguration.STATUS_NORMAL);
        applicationConfiguration.setValue(applicationConfigurationDO.getValue());
        applicationConfiguration.setGmtModified(applicationConfigurationDO.getGmtModified());
        applicationConfiguration.setOverride(applicationConfigurationDO.getOverride());
        return applicationConfiguration;
    }

    private List<ApplicationConfiguration> mergeConfigurations(List<ApplicationConfiguration> applicationConfigurations,
                                                               List<ApplicationConfiguration> defaultApplicationConfigurations) {
        List<ApplicationConfiguration> result = new ArrayList<>();
        if (applicationConfigurations.isEmpty()) {
            return defaultApplicationConfigurations;
        }
        Map<String, ApplicationConfiguration> defaultAliasToConfigs = defaultApplicationConfigurations.stream()
                .collect(
                        Collectors.toMap(ApplicationConfiguration::getAlias, e -> e));
        Map<String, ApplicationConfiguration> aliasToApplicationConfigurations = applicationConfigurations
                .stream().collect(
                        Collectors.toMap(ApplicationConfiguration::getAlias, e -> e));
        for (Map.Entry<String, ApplicationConfiguration> stringApplicationConfigurationEntry :
                aliasToApplicationConfigurations
                        .entrySet()) {
            //合并原有的配置
            mergeConfiguration(stringApplicationConfigurationEntry.getValue(),
                    defaultAliasToConfigs.get(stringApplicationConfigurationEntry.getKey()));
            result.add(stringApplicationConfigurationEntry.getValue());
        }
        for (Map.Entry<String, ApplicationConfiguration> stringApplicationConfigurationEntry :
                defaultAliasToConfigs
                        .entrySet()) {
            //补上新增的演练配置
            if (aliasToApplicationConfigurations.get(stringApplicationConfigurationEntry.getKey()) == null) {
                result.add(stringApplicationConfigurationEntry.getValue());
            }
        }
        return result;

    }

    private void mergeConfiguration(ApplicationConfiguration now, ApplicationConfiguration origin) {
        if (origin == null) {
            now.setStatus(ApplicationConfiguration.STATUS_INVALID);
            return;
        }
        now.setPriority(origin.getPriority());
        now.setDescription(origin.getDescription());
        now.setComponent(origin.getComponent());
        now.setAppType(origin.getAppType());
        now.setName(origin.getName());
        now.setAppType(origin.getAppType());
        now.setPhaseFlag(origin.getPhaseFlag());
        if(null == now.getScope()) {
            now.setScope(new ConfigurationScope());
        }
        now.getScope().setAppCodes(origin.getScope().getAppCodes());
        now.setFunctionParamAlias(origin.getFunctionParamAlias());
        if (now.getOverride() == null) {
            now.setOverride(origin.getOverride());
        }
    }
}
