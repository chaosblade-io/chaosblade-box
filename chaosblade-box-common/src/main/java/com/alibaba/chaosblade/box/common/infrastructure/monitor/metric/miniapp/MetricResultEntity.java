package com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class MetricResultEntity {

    private String host;

    private Number value;

    private String name;

    private String unit;

    private Long timestamp;

}
