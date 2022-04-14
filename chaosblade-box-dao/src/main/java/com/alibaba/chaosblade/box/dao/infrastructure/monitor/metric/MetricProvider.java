package com.alibaba.chaosblade.box.dao.infrastructure.monitor.metric;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.infrastructure.metric.MetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MetricDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author haibin.lhb
 *
 *
 */
public interface MetricProvider {

    public static String HIT_COUNT_CATEGORY = "hit";

    public static String KEY_CHAOSBLADE_EXP_OBJECT = "expObject";

    String CATEGORY_PREFIX = "arms-";

    /**
     * provide metrics
     *
     * @param metricDefinition
     * @param metricRequest
     * @return
     */
    public List<ChaosMetricEntity> provide(MetricDefinition metricDefinition, MetricRequest metricRequest);

    public Map<Host, List<ChaosMetricEntity>> batchProvider(List<Host> hosts, MetricDefinition metricDefinition,
                                                         MetricRequest metricRequest);

}
