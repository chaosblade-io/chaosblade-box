package com.alibaba.chaosblade.box.service.infrastructure.config;

import com.alibaba.chaosblade.box.service.NamespaceService;
import com.alibaba.chaosblade.box.service.impl.CloudNamespaceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haibin.lhb
 *
 * 
 */
@Configuration
public class CloudServiceConfiguration {

    @ConditionalOnMissingBean
    @Bean(name = "cloudNamespaceImpl")
    public NamespaceService namespaceService() {
        return new CloudNamespaceImpl();
    }

}
