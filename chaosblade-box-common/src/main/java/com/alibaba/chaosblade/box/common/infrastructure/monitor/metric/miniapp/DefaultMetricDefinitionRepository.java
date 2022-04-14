package com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
public class DefaultMetricDefinitionRepository implements MetricDefinitionRepository {

    private static String filePath = "metric.json";

    private static List<MetricDefinition> internalMetricDefinitions = new ArrayList<>();

    private static Map<String, List<MetricDefinition>> categoryToMetrics = new HashMap<>();

    static {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        try {
            Type type = new TypeReference<List<MetricDefinition>>() {}.getType();
            internalMetricDefinitions = JSON.parseObject(inputStream, type);
            categoryToMetrics = internalMetricDefinitions.stream().collect(
                Collectors.groupingBy(MetricDefinition::getCategory));
        } catch (Throwable throwable) {
        }
    }

    @Override
    public Optional<MetricDefinition> findByMetric(String metricKey) {
        return internalMetricDefinitions.stream().filter(new Predicate<MetricDefinition>() {
            @Override
            public boolean test(MetricDefinition internalMetricDefinition) {
                return internalMetricDefinition.getKey().equals(metricKey);
            }
        }).findFirst();
    }

    @Override
    public List<MetricDefinition> findByCategory(String category) {
        return categoryToMetrics.getOrDefault(category, new ArrayList<>());
    }
}
