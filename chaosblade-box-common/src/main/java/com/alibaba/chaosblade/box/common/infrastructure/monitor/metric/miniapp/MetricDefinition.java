package com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class MetricDefinition {

    private String key;

    private String name;

    private String category;

    private String unit;
}
