package com.alibaba.chaosblade.box.dao.infrastructure.service;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.metric.MetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.metric.UserDefinitionMetricRequest;

import java.util.List;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
public interface MetricService {

    public Response<List<ChaosMetricEntity>> getTargetMetric(MetricRequest metricRequest);

    public Response<Map<String, List<ChaosMetricEntity>>> batchGetTargetsMetric(List<Host> hosts,
                                                                             MetricRequest metricRequest);

    /**
     * 根据metric AppCode来获取下来指标
     * 比如 chaosapp.metric.cpu,那么cpu就是category
     *
     * @param metricAppCode
     * @return
     */
    public Response<List<String>> getMetricKeysByAppCode(String metricAppCode);

    public Response<List<ChaosMetricEntity>> getUserDefinitionMetric(
        UserDefinitionMetricRequest userDefinitionMetricRequest);

}
