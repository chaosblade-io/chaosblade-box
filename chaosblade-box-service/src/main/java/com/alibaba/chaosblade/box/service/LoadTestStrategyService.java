package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyCreateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyUpdateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyVO;

import java.util.List;

/**
 * 压测策略服务接口
 *
 * @author ZhengMingZhuo
 */
public interface LoadTestStrategyService {

    /**
     * 创建压测策略
     *
     * @param request 创建请求
     * @return 创建结果
     */
    Response<String> createLoadTestStrategy(LoadTestStrategyCreateRequest request);

    /**
     * 更新压测策略
     *
     * @param request 更新请求
     * @return 更新结果
     */
    Response<Void> updateLoadTestStrategy(LoadTestStrategyUpdateRequest request);

    /**
     * 删除压测策略
     *
     * @param strategyId 策略ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 删除结果
     */
    Response<Void> deleteLoadTestStrategy(String strategyId, String userId, String namespace);

    /**
     * 根据ID查询压测策略
     *
     * @param strategyId 策略ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测策略详情
     */
    Response<LoadTestStrategyVO> getLoadTestStrategy(String strategyId, String userId, String namespace);

    /**
     * 根据实验ID查询压测策略
     *
     * @param experimentId 实验ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测策略详情
     */
    Response<LoadTestStrategyVO> getLoadTestStrategyByExperimentId(String experimentId, String userId, String namespace);

    /**
     * 根据压测定义ID查询压测策略列表
     *
     * @param definitionId 压测定义ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测策略列表
     */
    Response<List<LoadTestStrategyVO>> getLoadTestStrategiesByDefinitionId(String definitionId, String userId, String namespace);

    /**
     * 分页查询压测策略
     *
     * @param request 查询请求
     * @return 分页结果
     */
    Response<PageableResponse<LoadTestStrategyVO>> queryLoadTestStrategies(LoadTestStrategyQueryRequest request);

    /**
     * 查询所有压测策略（不分页）
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测策略列表
     */
    Response<List<LoadTestStrategyVO>> listAllLoadTestStrategies(String userId, String namespace);
}
