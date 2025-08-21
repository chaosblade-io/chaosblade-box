package com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model;

import lombok.Data;

/**
 * 压测结果响应
 *
 * @author ZhengMingZhuo
 */
@Data
public class LoadTestResultResponse {

    /**
     * 执行ID
     */
    private String executionId;

    /**
     * 状态
     */
    private String status;

    /**
     * 结果文件相对路径（results/.../results.jtl）
     */
    private String resultPath;

    /**
     * 报告目录相对路径（reports/...）
     */
    private String reportPath;

    /**
     * 日志文件相对路径
     */
    private String logPath;

    /**
     * 供浏览器访问的结果文件相对 URL（/results/.../results.jtl）
     */
    private String resultUrl;

    /**
     * 供浏览器访问的报告首页（/reports/.../index.html）
     */
    private String reportUrl;

    /**
     * 压测定义的目标端点
     */
    private String endpoint;
}
