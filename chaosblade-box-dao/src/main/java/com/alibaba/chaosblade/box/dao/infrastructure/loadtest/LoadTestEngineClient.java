package com.alibaba.chaosblade.box.dao.infrastructure.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.*;

/**
 * 压测引擎客户端接口
 *
 * @author ZhengMingZhuo
 */
public interface LoadTestEngineClient {

    /**
     * 启动压测
     *
     * @param request 启动请求
     * @return 启动响应
     */
    Response<LoadTestExecutionResponse> startLoadTest(LoadTestStartRequest request);

    /**
     * 停止压测
     *
     * @param executionId 执行ID
     * @return 停止响应
     */
    Response<Boolean> stopLoadTest(String executionId);

    /**
     * 查询压测状态
     *
     * @param executionId 执行ID
     * @return 状态响应
     */
    Response<LoadTestStatusResponse> getLoadTestStatus(String executionId);

    /**
     * 获取压测结果
     *
     * @param executionId 执行ID
     * @return 结果响应
     */
    Response<LoadTestResultResponse> getLoadTestResults(String executionId);

    /**
     * 获取压测事件流水
     *
     * @param executionId 执行ID
     * @param tail 返回最近N条记录
     * @return 事件流水响应
     */
    Response<LoadTestEventsResponse> getLoadTestEvents(String executionId, Integer tail);
}
