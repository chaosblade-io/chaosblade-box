package com.alibaba.chaosblade.box.dao.repository.impl;

import com.alibaba.chaosblade.box.dao.mapper.LoadTestTaskMapper;
import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.LoadTestTaskRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 压测任务Repository实现类
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Repository
public class LoadTestTaskRepositoryImpl implements LoadTestTaskRepository {

    @Resource
    private LoadTestTaskMapper loadTestTaskMapper;

    @Override
    public boolean save(LoadTestTaskDO loadTestTaskDO) {
        try {
            if (loadTestTaskDO.getGmtCreate() == null) {
                loadTestTaskDO.setGmtCreate(new Date());
            }
            if (loadTestTaskDO.getGmtModified() == null) {
                loadTestTaskDO.setGmtModified(new Date());
            }
            if (loadTestTaskDO.getIsDelete() == null) {
                loadTestTaskDO.setIsDelete(0);
            }
            return loadTestTaskMapper.insert(loadTestTaskDO) > 0;
        } catch (Exception e) {
            log.error("保存压测任务失败", e);
            return false;
        }
    }

    @Override
    public Optional<LoadTestTaskDO> findByTaskId(String taskId) {
        try {
            LambdaQueryWrapper<LoadTestTaskDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LoadTestTaskDO::getTaskId, taskId)
                        .eq(LoadTestTaskDO::getIsDelete, 0);
            LoadTestTaskDO result = loadTestTaskMapper.selectOne(queryWrapper);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("根据任务ID查询压测任务失败", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<LoadTestTaskDO> findByExperimentTaskId(String experimentTaskId) {
        try {
            LoadTestTaskDO result = loadTestTaskMapper.selectByExperimentTaskId(experimentTaskId);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("根据演练任务ID查询压测任务失败", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<LoadTestTaskDO> findByExecutionId(String executionId) {
        try {
            LoadTestTaskDO result = loadTestTaskMapper.selectByExecutionId(executionId);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("根据执行ID查询压测任务失败", e);
            return Optional.empty();
        }
    }

    @Override
    public List<LoadTestTaskDO> findByStrategyId(String strategyId) {
        try {
            return loadTestTaskMapper.selectByStrategyId(strategyId);
        } catch (Exception e) {
            log.error("根据策略ID查询压测任务失败", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<LoadTestTaskDO> findRunningTasks(String userId, String namespace) {
        try {
            return loadTestTaskMapper.selectRunningTasks(userId, namespace);
        } catch (Exception e) {
            log.error("查询正在运行的压测任务失败", e);
            return Collections.emptyList();
        }
    }

    @Override
    public PageableResponse<LoadTestTaskDO> findByPage(int pageNum, int pageSize,
                                                       String userId, String namespace,
                                                       String strategyId, String experimentTaskId,
                                                       String status) {
        try {
            Page<LoadTestTaskDO> page = new Page<>(pageNum, pageSize);
            Page<LoadTestTaskDO> result = loadTestTaskMapper.selectByPage(
                    page, userId, namespace, strategyId, experimentTaskId, status);
            
            return PageableResponse.of(
                    pageNum,
                    pageSize,
                    result.getRecords(),
                    (int) result.getPages(),
                    result.getTotal());
        } catch (Exception e) {
            log.error("分页查询压测任务失败", e);
            return PageableResponse.of(
                    pageNum,
                    pageSize,
                    Collections.emptyList(),
                    0,
                    0L);
        }
    }

    @Override
    public boolean updateStatus(String taskId, String status, String errorMessage) {
        try {
            return loadTestTaskMapper.updateStatus(taskId, status, errorMessage) > 0;
        } catch (Exception e) {
            log.error("更新压测任务状态失败", e);
            return false;
        }
    }

    @Override
    public boolean updateExecutionInfo(String taskId, String executionId, String status, Date startTime) {
        try {
            return loadTestTaskMapper.updateExecutionInfo(taskId, executionId, status, startTime) > 0;
        } catch (Exception e) {
            log.error("更新压测任务执行信息失败", e);
            return false;
        }
    }

    @Override
    public boolean updateCompletionInfo(String taskId, String status, Date endTime,
                                        String resultPath, String reportPath, String logPath,
                                        String errorMessage) {
        try {
            return loadTestTaskMapper.updateCompletionInfo(
                    taskId, status, endTime, resultPath, reportPath, logPath, errorMessage) > 0;
        } catch (Exception e) {
            log.error("更新压测任务完成信息失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteByTaskId(String taskId) {
        try {
            LambdaUpdateWrapper<LoadTestTaskDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(LoadTestTaskDO::getTaskId, taskId)
                         .set(LoadTestTaskDO::getIsDelete, 1)
                         .set(LoadTestTaskDO::getGmtModified, new Date());
            return loadTestTaskMapper.update(null, updateWrapper) > 0;
        } catch (Exception e) {
            log.error("删除压测任务失败", e);
            return false;
        }
    }

    @Override
    public List<LoadTestTaskDO> findAll(String userId, String namespace) {
        try {
            LambdaQueryWrapper<LoadTestTaskDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LoadTestTaskDO::getUserId, userId)
                        .eq(LoadTestTaskDO::getNamespace, namespace)
                        .eq(LoadTestTaskDO::getIsDelete, 0)
                        .orderByDesc(LoadTestTaskDO::getGmtCreate);
            return loadTestTaskMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("查询所有压测任务失败", e);
            return Collections.emptyList();
        }
    }
}
