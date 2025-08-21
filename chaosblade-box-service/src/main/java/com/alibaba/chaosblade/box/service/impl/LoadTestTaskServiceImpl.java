package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.LoadTestTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.LoadTestEventsResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.LoadTestResultResponse;
import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.LoadTestTaskRepository;
import com.alibaba.chaosblade.box.service.LoadTestTaskService;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestTaskQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestTaskVO;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.PerformanceTimeseries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 压测任务服务实现类
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Service
public class LoadTestTaskServiceImpl implements LoadTestTaskService {

    @Resource
    private LoadTestTaskRepository loadTestTaskRepository;

    @Resource
    private LoadTestTaskManager loadTestTaskManager;

    @Override
    public Response<LoadTestTaskVO> getLoadTestTask(String taskId, String userId, String namespace) {
        try {
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByTaskId(taskId);
            if (!taskOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测任务不存在");
            }

            LoadTestTaskDO task = taskOptional.get();

            // 权限检查
            if (!task.getUserId().equals(userId) || !task.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            LoadTestTaskVO vo = LoadTestTaskVO.from(task);
            return Response.okWithData(vo);

        } catch (Exception e) {
            log.error("查询压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<LoadTestTaskVO> getLoadTestTaskByExperimentTaskId(String experimentTaskId, String userId, String namespace) {
        try {
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByExperimentTaskId(experimentTaskId);
            if (!taskOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "该演练未关联压测任务");
            }

            LoadTestTaskDO task = taskOptional.get();

            // 权限检查
            if (!task.getUserId().equals(userId) || !task.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            LoadTestTaskVO vo = LoadTestTaskVO.from(task);
            return Response.okWithData(vo);

        } catch (Exception e) {
            log.error("根据演练任务ID查询压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<PageableResponse<LoadTestTaskVO>> queryLoadTestTasks(LoadTestTaskQueryRequest request) {
        try {
            // 参数校验
            if (request.getUser() == null) {
                return Response.failedWith(CommonErrorCode.P_LOGIN_MISSED);
            }

            String userId = request.getUser().getUserId();
            String namespace = StringUtils.hasText(request.getNamespace()) ?
                    request.getNamespace() : "default";

            // 分页查询
            PageableResponse<LoadTestTaskDO> pageResult = loadTestTaskRepository.findByPage(
                    request.getPageNum(), request.getPageSize(),
                    userId, namespace, request.getStrategyId(), 
                    request.getExperimentTaskId(), request.getStatus());

            // 转换为VO
            List<LoadTestTaskVO> voList = pageResult.getData().stream()
                    .map(LoadTestTaskVO::from)
                    .collect(Collectors.toList());

            PageableResponse<LoadTestTaskVO> result = PageableResponse.of(
                    pageResult.getPage(),
                    pageResult.getPageSize(),
                    voList,
                    pageResult.getPages(),
                    pageResult.getTotal());

            return Response.okWithData(result);

        } catch (Exception e) {
            log.error("分页查询压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<List<LoadTestTaskVO>> listAllLoadTestTasks(String userId, String namespace) {
        try {
            List<LoadTestTaskDO> tasks = loadTestTaskRepository.findAll(userId, namespace);

            List<LoadTestTaskVO> voList = tasks.stream()
                    .map(LoadTestTaskVO::from)
                    .collect(Collectors.toList());

            return Response.okWithData(voList);

        } catch (Exception e) {
            log.error("查询所有压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<Boolean> stopLoadTestTask(String taskId, String userId, String namespace) {
        try {
            // 权限检查
            Response<LoadTestTaskVO> taskResponse = getLoadTestTask(taskId, userId, namespace);
            if (!taskResponse.isSuccess()) {
                return Response.failedWith(taskResponse.getError());
            }

            // 调用管理器停止任务
            return loadTestTaskManager.stopLoadTestTask(taskId);

        } catch (Exception e) {
            log.error("停止压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<LoadTestResultResponse> getLoadTestResults(String taskId, String experimentTaskId, String userId, String namespace) {
        try {
            // 根据参数确定实际的 taskId
            String actualTaskId = resolveTaskId(taskId, experimentTaskId, userId, namespace);
            if (actualTaskId == null) {
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "必须提供 taskId 或 experimentTaskId 中的一个");
            }

            // 权限检查
            Response<LoadTestTaskVO> taskResponse = getLoadTestTask(actualTaskId, userId, namespace);
            if (!taskResponse.isSuccess()) {
                return Response.failedWith(taskResponse.getError());
            }

            // 调用管理器获取结果
            return loadTestTaskManager.getLoadTestResults(actualTaskId);

        } catch (Exception e) {
            log.error("获取压测结果失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<LoadTestEventsResponse> getLoadTestEvents(String taskId, String experimentTaskId, Integer tail, String userId, String namespace) {
        try {
            // 根据参数确定实际的 taskId
            String actualTaskId = resolveTaskId(taskId, experimentTaskId, userId, namespace);
            if (actualTaskId == null) {
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "必须提供 taskId 或 experimentTaskId 中的一个");
            }

            // 权限检查
            Response<LoadTestTaskVO> taskResponse = getLoadTestTask(actualTaskId, userId, namespace);
            if (!taskResponse.isSuccess()) {
                return Response.failedWith(taskResponse.getError());
            }

            // 调用管理器获取事件流水
            return loadTestTaskManager.getLoadTestEvents(actualTaskId, tail);

        } catch (Exception e) {
            log.error("获取压测事件流水失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<LoadTestTaskVO> syncLoadTestTaskStatus(String taskId, String userId, String namespace) {
        try {
            // 权限检查
            Response<LoadTestTaskVO> taskResponse = getLoadTestTask(taskId, userId, namespace);
            if (!taskResponse.isSuccess()) {
                return Response.failedWith(taskResponse.getError());
            }

            // 调用管理器同步状态
            Response<LoadTestTaskDO> syncResponse = loadTestTaskManager.syncLoadTestTaskStatus(taskId);
            if (!syncResponse.isSuccess()) {
                return Response.failedWith(syncResponse.getError());
            }

            LoadTestTaskVO vo = LoadTestTaskVO.from(syncResponse.getResult());
            return Response.okWithData(vo);

        } catch (Exception e) {
            log.error("同步压测任务状态失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<PerformanceTimeseries> getPerformanceTimeseries(String executionId, String userId, String namespace) {
        try {
            // 调用管理器获取性能指标时序数据
            return loadTestTaskManager.getPerformanceTimeseries(executionId);

        } catch (Exception e) {
            log.error("获取性能指标时序数据失败: executionId={}", executionId, e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    /**
     * 根据 taskId 或 experimentTaskId 解析出实际的 taskId
     */
    private String resolveTaskId(String taskId, String experimentTaskId, String userId, String namespace) {
        // 如果直接提供了 taskId，直接返回
        if (taskId != null && !taskId.trim().isEmpty()) {
            return taskId;
        }

        // 如果提供了 experimentTaskId，查找对应的压测任务
        if (experimentTaskId != null && !experimentTaskId.trim().isEmpty()) {
            try {
                // 通过 experimentTaskId 查找演练任务，然后查找关联的压测任务
                return loadTestTaskManager.findTaskIdByExperimentId(experimentTaskId, userId, namespace);
            } catch (Exception e) {
                log.error("根据 experimentTaskId 查找压测任务失败: experimentTaskId={}", experimentTaskId, e);
            }
        }

        return null;
    }

    @Override
    public Response<LoadTestResultResponse> getLoadTestResultsWithEndpoint(String taskId, String experimentTaskId, String userId, String namespace) {
        try {
            // 根据参数确定实际的 taskId
            String actualTaskId = resolveTaskId(taskId, experimentTaskId, userId, namespace);
            if (actualTaskId == null) {
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "必须提供 taskId 或 experimentTaskId 中的一个");
            }

            // 权限检查
            Response<LoadTestTaskVO> taskResponse = getLoadTestTask(actualTaskId, userId, namespace);
            if (!taskResponse.isSuccess()) {
                return Response.failedWith(taskResponse.getError());
            }

            // 调用管理器获取压测结果
            Response<LoadTestResultResponse> resultResponse = loadTestTaskManager.getLoadTestResults(actualTaskId);

            if (resultResponse.isSuccess() && resultResponse.getResult() != null) {
                // 获取压测任务信息以获取endpoint
                LoadTestTaskVO taskVO = taskResponse.getResult();

                // 获取压测定义的endpoint信息
                String endpoint = loadTestTaskManager.getLoadTestEndpoint(actualTaskId);

                // 设置endpoint到结果中
                LoadTestResultResponse result = resultResponse.getResult();
                result.setEndpoint(endpoint);

                log.info("获取压测结果成功并设置endpoint: taskId={}, endpoint={}", actualTaskId, endpoint);
            }

            return resultResponse;

        } catch (Exception e) {
            log.error("获取压测结果失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }
}
