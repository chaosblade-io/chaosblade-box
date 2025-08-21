package com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model;

import lombok.Data;

import java.util.List;

/**
 * 性能指标时序数据
 *
 * @author ZhengMingZhuo
 */
@Data
public class PerformanceTimeseries {

    /**
     * 时间桶大小（毫秒）
     */
    private Long bucketSizeMs;

    /**
     * 平均延迟时序 [timestamp, value]
     */
    private List<List<Object>> avgLatency;

    /**
     * 最小延迟时序 [timestamp, value]
     */
    private List<List<Object>> minLatency;

    /**
     * 最大延迟时序 [timestamp, value]
     */
    private List<List<Object>> maxLatency;

    /**
     * P90延迟时序 [timestamp, value]
     */
    private List<List<Object>> p90;

    /**
     * P95延迟时序 [timestamp, value]
     */
    private List<List<Object>> p95;

    /**
     * P99延迟时序 [timestamp, value]
     */
    private List<List<Object>> p99;

    /**
     * 成功率时序 [timestamp, value%]
     */
    private List<List<Object>> successRate;

    /**
     * 接收吞吐量时序 [timestamp, value bytes/s]
     */
    private List<List<Object>> throughputReceived;

    /**
     * 发送吞吐量时序 [timestamp, value bytes/s]
     */
    private List<List<Object>> throughputSent;
}
