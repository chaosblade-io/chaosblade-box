package com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model;

import lombok.Data;

import java.util.Map;

/**
 * 压测状态响应
 *
 * @author ZhengMingZhuo
 */
@Data
public class LoadTestStatusResponse {

    /**
     * 执行 ID
     */
    private String executionId;

    /**
     * 任务名
     */
    private String testName;

    /**
     * 描述
     */
    private String description;

    /**
     * 测试计划路径
     */
    private String testPlanPath;

    /**
     * 容器ID
     */
    private String containerId;

    /**
     * 容器名称
     */
    private String containerName;

    /**
     * 状态：PENDING|RUNNING|COMPLETED|FAILED|STOPPED|TIMEOUT
     */
    private String status;

    /**
     * 开始时间（ISO 8601格式）
     */
    private String startTime;

    /**
     * 结束时间（ISO 8601格式，可空）
     */
    private String endTime;

    /**
     * 结果文件相对路径，如 results/20250101_120000_abcd1234/results.jtl
     */
    private String resultPath;

    /**
     * 报告目录相对路径，如 reports/20250101_120000_abcd1234
     */
    private String reportPath;

    /**
     * 日志文件相对路径
     */
    private String logPath;

    /**
     * 运行参数
     */
    private Map<String, Object> parameters;

    /**
     * 错误信息
     */
    private String errorMessage;
}
