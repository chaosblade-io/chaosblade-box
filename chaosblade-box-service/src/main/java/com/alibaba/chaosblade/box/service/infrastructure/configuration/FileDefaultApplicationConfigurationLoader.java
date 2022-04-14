package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class FileDefaultApplicationConfigurationLoader implements DefaultApplicationConfigurationLoader,
    InitializingBean {

    private String PATH = "application/application_configuration.json";

    private List<ApplicationConfiguration> applicationConfigurations = new ArrayList<>();

    @Override
    public List<ApplicationConfiguration> loadConfigurations() {
        return applicationConfigurations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH)) {
            assert inputStream != null;
            applicationConfigurations = JSON.parseObject(inputStream,
                new TypeReference<List<ApplicationConfiguration>>() {}.getType());
            //check not exist,if not equal,has duplicate items,error
            int totalSize = applicationConfigurations.stream().map(ApplicationConfiguration::getAlias).collect(
                Collectors.toSet())
                .size();
            assert totalSize == applicationConfigurations.size();
        }
    }
}
