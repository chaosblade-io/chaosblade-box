package com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp;

import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
public interface MetricDefinitionRepository {

    /**
     * 根据具体的metric获取metric定义
     *
     * @param metricKey
     * @return
     */
    public <T extends MetricDefinition> Optional<T> findByMetric(String metricKey);

    /**
     * 根据category获取所有
     * @param category
     * @return
     */
    public <T extends MetricDefinition> List<T> findByCategory(String category);

}
