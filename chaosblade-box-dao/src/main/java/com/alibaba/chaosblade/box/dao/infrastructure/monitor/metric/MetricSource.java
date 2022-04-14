package com.alibaba.chaosblade.box.dao.infrastructure.monitor.metric;

/**
 * @author haibin.lhb
 *
 * 
 */
public enum MetricSource {

    ChaosBlade(0),
    Chaos(1);

    public int getValue() {
        return value;
    }

    private int value;

    MetricSource(int type) {
        this.value = type;
    }
}
