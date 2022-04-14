package com.alibaba.chaosblade.box;

import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.DefaultMetricDefinitionRepository;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MetricDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haibin
 *
 *
 */
@Configuration
public class PublicCloudAutoConfiguration {

    @Bean
    MetricDefinitionRepository metricDefinitionRepository() {
        return new DefaultMetricDefinitionRepository();
    }
}