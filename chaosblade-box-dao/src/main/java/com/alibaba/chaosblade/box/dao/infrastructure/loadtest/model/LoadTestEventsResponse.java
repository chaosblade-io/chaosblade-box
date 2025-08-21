package com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model;

import lombok.Data;

import java.util.List;

/**
 * 压测事件流水响应
 *
 * @author ZhengMingZhuo
 */
@Data
public class LoadTestEventsResponse {

    /**
     * 执行ID
     */
    private String executionId;

    /**
     * 事件流水（每行一个 JSON 字符串）
     */
    private List<String> events;
}
