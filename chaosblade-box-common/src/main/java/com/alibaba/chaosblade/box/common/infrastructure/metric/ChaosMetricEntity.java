package com.alibaba.chaosblade.box.common.infrastructure.metric;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author haibin
 *
 * 
 */
@RequiredArgsConstructor
@Data
public class ChaosMetricEntity {

    private String userId;
    private String namespace;

    public ChaosMetricEntity(String userId, String namespace, String metric, Host host, Long timestamp, Number value) {
        this.userId = userId;
        this.namespace = namespace;
        this.metric = metric;
        this.host = host;
        this.timestamp = timestamp;
        this.value = value;
    }

    public ChaosMetricEntity(Host host, Long timestamp, Number value, String unit) {
        this.host = host;
        this.timestamp = timestamp;
        this.value = value;
        this.unit = unit;
    }

    private String metric;
    private Host host;

    private Long timestamp;

    private Number value;

    private String unit;
}
