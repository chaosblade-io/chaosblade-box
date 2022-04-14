package com.alibaba.chaosblade.box.service.infrastructure.miniapp;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.metric.MetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MetricDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
public class EmptyMetricServiceImpl extends BaseMetricServiceImpl {
    @Override
    public Response<List<ChaosMetricEntity>> getTargetMetric(MetricRequest metricRequest) {
        List<ChaosMetricEntity> chaosMetricEntities = new ArrayList<>();

        return new Response<>();
    }

    @Override
    protected List<ChaosMetricEntity> getMetrics(MetricDefinition metricDefinition, MetricRequest metricRequest) {
        return null;
    }

    @Override
    public Response<Map<String, List<ChaosMetricEntity>>> batchGetTargetsMetric(List<Host> hosts,
        MetricRequest metricRequest) {
        return null;
    }

    @Override
    public Response<List<String>> getMetricKeysByAppCode(String metricAppCode) {
        return null;
    }

}
