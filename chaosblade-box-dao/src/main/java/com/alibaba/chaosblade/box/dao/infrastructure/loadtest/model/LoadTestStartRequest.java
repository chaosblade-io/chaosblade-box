package com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model;

import lombok.Data;

import java.util.Map;

/**
 * 压测启动请求
 *
 * @author ZhengMingZhuo
 */
@Data
public class LoadTestStartRequest {

    /**
     * JMX 相对路径，如 uploads/testplans/xxx.jmx
     */
    private String testPlanPath;

    /**
     * 任务名
     */
    private String testName;

    /**
     * 描述
     */
    private String description;

    /**
     * 线程数，默认 1，≥1
     */
    private Integer threads;

    /**
     * 循环次数，默认 1，≥1
     */
    private Integer loops;

    /**
     * 启动时间（秒），默认 0，≥0
     */
    private Integer rampUp;

    /**
     * 持续时间（秒），≥1。提供时系统会生成运行时 JMX 副本并按此持续时间运行，同时作为优雅停止上限
     */
    private Integer duration;

    /**
     * 超时时间（秒），默认 3600，≥10。兜底上限
     */
    private Integer timeout;

    /**
     * JVM 堆大小，默认 "1g"。JVM 堆大小（如 "2g"）
     */
    private String heapSize;

    /**
     * 传递给 JMeter 的 -J 属性集
     */
    private Map<String, String> properties;

    /**
     * 传递 JVM 参数键值（由服务组装）
     */
    private Map<String, String> jvmArgs;

    /**
     * 构建器
     */
    public static LoadTestStartRequestBuilder builder() {
        return new LoadTestStartRequestBuilder();
    }

    public static class LoadTestStartRequestBuilder {
        private LoadTestStartRequest request = new LoadTestStartRequest();

        public LoadTestStartRequestBuilder testPlanPath(String testPlanPath) {
            request.setTestPlanPath(testPlanPath);
            return this;
        }

        public LoadTestStartRequestBuilder testName(String testName) {
            request.setTestName(testName);
            return this;
        }

        public LoadTestStartRequestBuilder description(String description) {
            request.setDescription(description);
            return this;
        }

        public LoadTestStartRequestBuilder threads(Integer threads) {
            request.setThreads(threads);
            return this;
        }

        public LoadTestStartRequestBuilder loops(Integer loops) {
            request.setLoops(loops);
            return this;
        }

        public LoadTestStartRequestBuilder rampUp(Integer rampUp) {
            request.setRampUp(rampUp);
            return this;
        }

        public LoadTestStartRequestBuilder duration(Integer duration) {
            request.setDuration(duration);
            return this;
        }

        public LoadTestStartRequestBuilder timeout(Integer timeout) {
            request.setTimeout(timeout);
            return this;
        }

        public LoadTestStartRequestBuilder heapSize(String heapSize) {
            request.setHeapSize(heapSize);
            return this;
        }

        public LoadTestStartRequestBuilder properties(Map<String, String> properties) {
            request.setProperties(properties);
            return this;
        }

        public LoadTestStartRequestBuilder jvmArgs(Map<String, String> jvmArgs) {
            request.setJvmArgs(jvmArgs);
            return this;
        }

        public LoadTestStartRequest build() {
            return request;
        }
    }
}
