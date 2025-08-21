package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.LoadTestStrategyMapper;
import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 压测策略 Repository
 *
 * @author ZhengMingZhuo
 */
@Repository
public class LoadTestStrategyRepository implements IRepository<String, LoadTestStrategyDO> {

    @Resource
    @Getter
    private LoadTestStrategyMapper loadTestStrategyMapper;

    @Override
    public Optional<LoadTestStrategyDO> findById(String strategyId) {
        QueryWrapper<LoadTestStrategyDO> queryWrapper = new QueryWrapper<LoadTestStrategyDO>()
                .eq("strategy_id", strategyId)
                .eq("is_delete", 0);
        return Optional.ofNullable(loadTestStrategyMapper.selectOne(queryWrapper));
    }

    @Override
    public boolean update(LoadTestStrategyDO loadTestStrategy) {
        int rows = loadTestStrategyMapper.updateById(loadTestStrategy);
        return rows > 0;
    }

    @Override
    public boolean add(LoadTestStrategyDO loadTestStrategy) {
        int rows = loadTestStrategyMapper.insert(loadTestStrategy);
        return rows > 0;
    }

    /**
     * 分页查询压测策略
     *
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param userId 用户ID
     * @param namespace 命名空间
     * @param definitionId 压测定义ID（可选）
     * @param experimentId 实验ID（可选）
     * @param enable 是否启用（可选）
     * @return 分页结果
     */
    public PageableResponse<LoadTestStrategyDO> findByPage(int pageNo, int pageSize,
                                                         String userId, String namespace,
                                                         String definitionId, String experimentId,
                                                         Integer enable) {
        IPage<LoadTestStrategyDO> page = MyBatisUtil.getPage(pageNo, pageSize);
        IPage<LoadTestStrategyDO> result = loadTestStrategyMapper.selectPageByConditions(
                page, userId, namespace, definitionId, experimentId, enable);

        if (result == null) {
            return PageableResponse.of(pageNo, pageSize, Lists.newArrayList());
        }

        return PageableResponse.of(pageNo, pageSize, result.getRecords())
                .pages(result.getPages())
                .total(result.getTotal());
    }

    /**
     * 查询所有压测策略（不分页）
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测策略列表
     */
    public List<LoadTestStrategyDO> findAll(String userId, String namespace) {
        QueryWrapper<LoadTestStrategyDO> queryWrapper = new QueryWrapper<LoadTestStrategyDO>()
                .eq("is_delete", 0);

        if (userId != null && !userId.isEmpty()) {
            queryWrapper.eq("user_id", userId);
        }

        if (namespace != null && !namespace.isEmpty()) {
            queryWrapper.eq("namespace", namespace);
        }

        queryWrapper.orderByDesc("gmt_create");

        return loadTestStrategyMapper.selectList(queryWrapper);
    }

    /**
     * 逻辑删除压测策略
     *
     * @param strategyId 策略ID
     * @return 是否删除成功
     */
    public boolean deleteById(String strategyId) {
        LoadTestStrategyDO loadTestStrategy = new LoadTestStrategyDO();
        loadTestStrategy.setStrategyId(strategyId);
        loadTestStrategy.setIsDelete(1);
        return update(loadTestStrategy);
    }

    /**
     * 根据实验ID查询压测策略
     *
     * @param experimentId 实验ID
     * @return 压测策略
     */
    public Optional<LoadTestStrategyDO> findByExperimentId(String experimentId) {
        LoadTestStrategyDO strategy = loadTestStrategyMapper.selectByExperimentId(experimentId);
        return Optional.ofNullable(strategy);
    }

    /**
     * 根据压测定义ID查询压测策略列表
     *
     * @param definitionId 压测定义ID
     * @return 压测策略列表
     */
    public List<LoadTestStrategyDO> findByDefinitionId(String definitionId) {
        return loadTestStrategyMapper.selectByDefinitionId(definitionId);
    }

    /**
     * 检查实验是否已绑定策略（排除指定策略ID）
     *
     * @param experimentId 实验ID
     * @param excludeStrategyId 排除的策略ID
     * @return 是否已绑定
     */
    public boolean existsByExperimentId(String experimentId, String excludeStrategyId) {
        QueryWrapper<LoadTestStrategyDO> queryWrapper = new QueryWrapper<LoadTestStrategyDO>()
                .eq("experiment_id", experimentId)
                .eq("is_delete", 0);

        if (excludeStrategyId != null && !excludeStrategyId.isEmpty()) {
            queryWrapper.ne("strategy_id", excludeStrategyId);
        }

        return loadTestStrategyMapper.selectCount(queryWrapper) > 0;
    }
}
