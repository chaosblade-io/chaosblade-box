package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.metric.MetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.metric.UserDefinitionMetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MetricDefinition;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.metric.MetricProvider;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.miniapp.BaseMetricServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haibin
 *
 * 
 */
@Component(value = "cloudMetricService")
@Slf4j
public class CloudMetricServiceImpl extends BaseMetricServiceImpl {

    @Autowired
    private List<MetricProvider> metricProviders;

    @Override
    protected List<ChaosMetricEntity> getMetrics(MetricDefinition metricDefinition, MetricRequest metricRequest) {
        return metricProviders.stream().flatMap(new Function<MetricProvider, Stream<ChaosMetricEntity>>() {
            @Override
            public Stream<ChaosMetricEntity> apply(MetricProvider metricProvider) {
                return Optional.ofNullable(metricProvider.provide(metricDefinition, metricRequest))
                    .orElse(new ArrayList<>()).stream();
            }
        }).collect(Collectors.toList());
    }

    @Override
    public Response<Map<String, List<ChaosMetricEntity>>> batchGetTargetsMetric(List<Host> list,
                                                                             MetricRequest metricRequest) {
        return null;
    }

    @Override
    public Response<List<ChaosMetricEntity>> getUserDefinitionMetric(
        UserDefinitionMetricRequest userDefinitionMetricRequest) {
        return null;
    }



}
