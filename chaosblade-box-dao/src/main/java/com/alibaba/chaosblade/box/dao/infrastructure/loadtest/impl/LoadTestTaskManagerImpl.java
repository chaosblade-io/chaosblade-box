package com.alibaba.chaosblade.box.dao.infrastructure.loadtest.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.LoadTestEngineClient;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.LoadTestTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.*;
import com.alibaba.chaosblade.box.dao.model.LoadTestDefinitionDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.alibaba.chaosblade.box.dao.repository.LoadTestDefinitionRepository;
import com.alibaba.chaosblade.box.dao.repository.LoadTestStrategyRepository;
import com.alibaba.chaosblade.box.dao.repository.LoadTestTaskRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 压测任务管理器实现类
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Component
public class LoadTestTaskManagerImpl implements LoadTestTaskManager {

    @Resource
    private LoadTestTaskRepository loadTestTaskRepository;

    @Resource
    private LoadTestStrategyRepository loadTestStrategyRepository;

    @Resource
    private LoadTestDefinitionRepository loadTestDefinitionRepository;

    @Resource
    private LoadTestEngineClient loadTestEngineClient;

    @Override
    public Response<LoadTestTaskDO> createAndStartLoadTestTask(String strategyId, String experimentTaskId, 
                                                               String userId, String namespace) {
        try {
            // 1. 查询压测策略
            Optional<LoadTestStrategyDO> strategyOptional = loadTestStrategyRepository.findById(strategyId);
            if (!strategyOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测策略不存在");
            }

            LoadTestStrategyDO strategy = strategyOptional.get();
            if (!strategy.getEnable().equals(1)) {
                return Response.failedWith(CommonErrorCode.B_EXPERIMENT_CREATE_FAILED, "压测策略未启用");
            }

            // 2. 查询压测定义
            Optional<LoadTestDefinitionDO> definitionOptional =
                    loadTestDefinitionRepository.findById(strategy.getDefinitionId());
            if (!definitionOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测定义不存在");
            }

            LoadTestDefinitionDO definition = definitionOptional.get();

            // 3. 检查是否已存在压测任务
            Optional<LoadTestTaskDO> existingTaskOptional = 
                    loadTestTaskRepository.findByExperimentTaskId(experimentTaskId);
            if (existingTaskOptional.isPresent()) {
                LoadTestTaskDO existingTask = existingTaskOptional.get();
                if (existingTask.isRunning()) {
                    return Response.failedWith(CommonErrorCode.B_EXPERIMENT_ALREADY_RUNNING, "压测任务已在运行中");
                }
            }

            // 4. 创建压测任务
            LoadTestTaskDO loadTestTask = new LoadTestTaskDO();
            loadTestTask.setTaskId(IdWorker.getIdStr());
            loadTestTask.setStrategyId(strategyId);
            loadTestTask.setExperimentTaskId(experimentTaskId);
            loadTestTask.setStatus(LoadTestTaskDO.Status.PENDING.getCode());
            loadTestTask.setUserId(userId);
            loadTestTask.setNamespace(namespace);

            boolean saved = loadTestTaskRepository.save(loadTestTask);
            if (!saved) {
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "保存压测任务失败");
            }

            // 5. 构建启动请求
            LoadTestStartRequest startRequest = buildStartRequest(definition, strategy);

            // 6. 调用压测引擎启动压测
            Response<LoadTestExecutionResponse> startResponse = loadTestEngineClient.startLoadTest(startRequest);
            if (!startResponse.isSuccess()) {
                // 启动失败，更新任务状态
                loadTestTaskRepository.updateStatus(loadTestTask.getTaskId(), 
                        LoadTestTaskDO.Status.FAILED.getCode(), startResponse.getError().getErrorMessage());
                return Response.failedWith(startResponse.getError());
            }

            // 7. 更新任务执行信息
            LoadTestExecutionResponse executionResponse = startResponse.getResult();
            loadTestTaskRepository.updateExecutionInfo(
                    loadTestTask.getTaskId(),
                    executionResponse.getExecutionId(),
                    executionResponse.getStatus(),
                    parseIsoDateTime(executionResponse.getStartTime())
            );

            // 8. 重新查询任务返回最新状态
            Optional<LoadTestTaskDO> updatedTaskOptional = 
                    loadTestTaskRepository.findByTaskId(loadTestTask.getTaskId());
            if (updatedTaskOptional.isPresent()) {
                return Response.okWithData(updatedTaskOptional.get());
            } else {
                return Response.okWithData(loadTestTask);
            }

        } catch (Exception e) {
            log.error("创建并启动压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "创建并启动压测任务失败: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> stopLoadTestTask(String taskId) {
        try {
            // 1. 查询压测任务
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByTaskId(taskId);
            if (!taskOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测任务不存在");
            }

            LoadTestTaskDO task = taskOptional.get();
            if (task.isFinished()) {
                return Response.okWithData(true); // 已完成的任务无需停止
            }

            // 2. 调用压测引擎停止压测
            if (StringUtils.hasText(task.getExecutionId())) {
                Response<Boolean> stopResponse = loadTestEngineClient.stopLoadTest(task.getExecutionId());
                if (!stopResponse.isSuccess()) {
                    log.warn("调用压测引擎停止失败: {}", stopResponse.getError().getErrorMessage());
                }
            }

            // 3. 更新任务状态为已停止
            boolean updated = loadTestTaskRepository.updateStatus(taskId, 
                    LoadTestTaskDO.Status.STOPPED.getCode(), null);
            
            return Response.okWithData(updated);

        } catch (Exception e) {
            log.error("停止压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "停止压测任务失败: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> stopLoadTestTaskByExperimentTaskId(String experimentTaskId) {
        try {
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByExperimentTaskId(experimentTaskId);
            if (!taskOptional.isPresent()) {
                return Response.okWithData(true); // 没有压测任务，认为停止成功
            }

            return stopLoadTestTask(taskOptional.get().getTaskId());

        } catch (Exception e) {
            log.error("根据演练任务ID停止压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "停止压测任务失败: " + e.getMessage());
        }
    }

    @Override
    public Response<LoadTestTaskDO> getLoadTestTaskStatus(String taskId) {
        try {
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByTaskId(taskId);
            if (!taskOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测任务不存在");
            }

            LoadTestTaskDO task = taskOptional.get();
            
            // 如果任务正在运行，尝试同步最新状态
            if (task.isRunning() && StringUtils.hasText(task.getExecutionId())) {
                syncLoadTestTaskStatus(taskId);
                // 重新查询最新状态
                taskOptional = loadTestTaskRepository.findByTaskId(taskId);
                if (taskOptional.isPresent()) {
                    task = taskOptional.get();
                }
            }

            return Response.okWithData(task);

        } catch (Exception e) {
            log.error("查询压测任务状态失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "查询压测任务状态失败: " + e.getMessage());
        }
    }

    @Override
    public Response<LoadTestTaskDO> getLoadTestTaskStatusByExperimentTaskId(String experimentTaskId) {
        try {
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByExperimentTaskId(experimentTaskId);
            if (!taskOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "该演练未关联压测任务");
            }

            return getLoadTestTaskStatus(taskOptional.get().getTaskId());

        } catch (Exception e) {
            log.error("根据演练任务ID查询压测任务状态失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "查询压测任务状态失败: " + e.getMessage());
        }
    }

    @Override
    public Response<LoadTestResultResponse> getLoadTestResults(String taskId) {
        try {
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByTaskId(taskId);
            if (!taskOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测任务不存在");
            }

            LoadTestTaskDO task = taskOptional.get();
            if (!StringUtils.hasText(task.getExecutionId())) {
                return Response.failedWith(CommonErrorCode.B_EXPERIMENT_CREATE_FAILED, "压测任务未启动");
            }

            return loadTestEngineClient.getLoadTestResults(task.getExecutionId());

        } catch (Exception e) {
            log.error("获取压测结果失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "获取压测结果失败: " + e.getMessage());
        }
    }

    @Override
    public Response<LoadTestEventsResponse> getLoadTestEvents(String taskId, Integer tail) {
        try {
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByTaskId(taskId);
            if (!taskOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测任务不存在");
            }

            LoadTestTaskDO task = taskOptional.get();
            if (!StringUtils.hasText(task.getExecutionId())) {
                return Response.failedWith(CommonErrorCode.B_EXPERIMENT_CREATE_FAILED, "压测任务未启动");
            }

            return loadTestEngineClient.getLoadTestEvents(task.getExecutionId(), tail);

        } catch (Exception e) {
            log.error("获取压测事件流水失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "获取压测事件流水失败: " + e.getMessage());
        }
    }

    @Override
    public Response<LoadTestTaskDO> syncLoadTestTaskStatus(String taskId) {
        try {
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByTaskId(taskId);
            if (!taskOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测任务不存在");
            }

            LoadTestTaskDO task = taskOptional.get();
            if (!StringUtils.hasText(task.getExecutionId())) {
                return Response.okWithData(task); // 没有执行ID，无需同步
            }

            // 查询压测引擎状态
            Response<LoadTestStatusResponse> statusResponse = 
                    loadTestEngineClient.getLoadTestStatus(task.getExecutionId());
            if (!statusResponse.isSuccess()) {
                log.warn("同步压测任务状态失败: {}", statusResponse.getError().getErrorMessage());
                return Response.okWithData(task);
            }

            LoadTestStatusResponse status = statusResponse.getResult();
            
            // 更新本地状态
            if (isFinishedStatus(status.getStatus())) {
                loadTestTaskRepository.updateCompletionInfo(
                        taskId,
                        status.getStatus(),
                        parseIsoDateTime(status.getEndTime()),
                        status.getResultPath(),
                        status.getReportPath(),
                        status.getLogPath(),
                        status.getErrorMessage()
                );
            } else {
                loadTestTaskRepository.updateStatus(taskId, status.getStatus(), status.getErrorMessage());
            }

            // 重新查询返回最新状态
            taskOptional = loadTestTaskRepository.findByTaskId(taskId);
            return Response.okWithData(taskOptional.orElse(task));

        } catch (Exception e) {
            log.error("同步压测任务状态失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "同步压测任务状态失败: " + e.getMessage());
        }
    }

    @Override
    public Response<Integer> handleTimeoutTasks(String userId, String namespace) {
        try {
            List<LoadTestTaskDO> runningTasks = loadTestTaskRepository.findRunningTasks(userId, namespace);
            int handledCount = 0;

            for (LoadTestTaskDO task : runningTasks) {
                try {
                    // 同步状态
                    syncLoadTestTaskStatus(task.getTaskId());
                    handledCount++;
                } catch (Exception e) {
                    log.warn("处理超时压测任务失败: taskId={}", task.getTaskId(), e);
                }
            }

            return Response.okWithData(handledCount);

        } catch (Exception e) {
            log.error("处理超时压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "处理超时压测任务失败: " + e.getMessage());
        }
    }

    private LoadTestStartRequest buildStartRequest(LoadTestDefinitionDO definition, LoadTestStrategyDO strategy) {
        log.info("构建压测启动请求: definition={}, strategy={}", definition, strategy);

        // 验证必需字段
        if (definition.getContentRef() == null || definition.getContentRef().trim().isEmpty()) {
            throw new IllegalArgumentException("压测定义的 contentRef 字段不能为空，请先上传 JMX 文件");
        }

        if (definition.getEndpoint() == null || definition.getEndpoint().trim().isEmpty()) {
            throw new IllegalArgumentException("压测定义的 endpoint 字段不能为空");
        }

        if (strategy.getTrafficDurationSec() == null || strategy.getTrafficDurationSec() <= 0) {
            throw new IllegalArgumentException("压测策略的 trafficDurationSec 必须大于 0");
        }

        // 处理 testPlanPath，确保是相对路径
        String testPlanPath = definition.getContentRef();
        if (testPlanPath.startsWith("/data/")) {
            testPlanPath = testPlanPath.substring(6); // 去掉 "/data/" 前缀
        }

        LoadTestStartRequest.LoadTestStartRequestBuilder builder = LoadTestStartRequest.builder()
                .testPlanPath(testPlanPath)
                .testName("Experiment Load Test - " + definition.getName())
                .description("Load test for experiment: " + strategy.getExperimentId())
                .threads(1) // 默认线程数
                .loops(1)   // 默认循环次数
                .rampUp(0)  // 默认启动时间
                .duration(strategy.getTrafficDurationSec())
                .timeout(strategy.getTrafficDurationSec() + 300) // 超时时间比持续时间多5分钟
                .heapSize("1g"); // 默认堆大小

        // 设置默认参数
        Map<String, String> properties = new HashMap<>();
        String host = extractHostFromEndpoint(definition.getEndpoint());
        String port = extractPortFromEndpoint(definition.getEndpoint());
        properties.put("host", host);
        properties.put("port", port);
        builder.properties(properties);

        LoadTestStartRequest request = builder.build();
        log.info("构建的压测启动请求: 原始路径={}, 转换后路径={}, host={}, port={}, duration={}",
                definition.getContentRef(), request.getTestPlanPath(), host, port, request.getDuration());

        return request;
    }

    private String extractHostFromEndpoint(String endpoint) {
        try {
            if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
                java.net.URL url = new java.net.URL(endpoint);
                return url.getHost();
            } else {
                String[] parts = endpoint.split(":");
                return parts[0];
            }
        } catch (Exception e) {
            return "localhost";
        }
    }

    private String extractPortFromEndpoint(String endpoint) {
        try {
            if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
                java.net.URL url = new java.net.URL(endpoint);
                int port = url.getPort();
                return port > 0 ? String.valueOf(port) : (endpoint.startsWith("https://") ? "443" : "80");
            } else {
                String[] parts = endpoint.split(":");
                return parts.length > 1 ? parts[1] : "80";
            }
        } catch (Exception e) {
            return "80";
        }
    }

    private Date parseIsoDateTime(String isoDateTime) {
        if (!StringUtils.hasText(isoDateTime)) {
            return null;
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME);
            return java.sql.Timestamp.valueOf(localDateTime);
        } catch (Exception e) {
            log.warn("解析时间失败: {}", isoDateTime, e);
            return new Date();
        }
    }

    private boolean isFinishedStatus(String status) {
        return "COMPLETED".equals(status) || "FAILED".equals(status) ||
               "STOPPED".equals(status) || "TIMEOUT".equals(status);
    }

    @Override
    public Response<PerformanceTimeseries> getPerformanceTimeseries(String executionId) {
        try {
            log.info("获取性能指标时序数据: executionId={}", executionId);

            // 调用压测引擎获取性能指标时序数据
            Response<PerformanceTimeseries> response = loadTestEngineClient.getPerformanceTimeseries(executionId);

            if (response.isSuccess()) {
                log.info("获取性能指标时序数据成功: executionId={}", executionId);
            } else {
                log.warn("获取性能指标时序数据失败: executionId={}, error={}", executionId, response.getError());
            }

            return response;

        } catch (Exception e) {
            log.error("获取性能指标时序数据异常: executionId={}", executionId, e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "获取性能指标时序数据失败: " + e.getMessage());
        }
    }

    @Override
    public String findTaskIdByExperimentId(String experimentId, String userId, String namespace) {
        try {
            log.info("根据演练ID查找压测任务: experimentId={}, userId={}, namespace={}", experimentId, userId, namespace);

            // 查找与演练ID关联的压测任务
            Optional<LoadTestTaskDO> taskOptional = loadTestTaskRepository.findByExperimentTaskId(experimentId);

            if (taskOptional.isPresent()) {
                LoadTestTaskDO task = taskOptional.get();
                // 检查用户权限和命名空间
                if (userId.equals(task.getUserId()) && namespace.equals(task.getNamespace())) {
                    log.info("找到匹配的压测任务: taskId={}", task.getTaskId());
                    return task.getTaskId();
                } else {
                    log.warn("权限不匹配: experimentId={}, userId={}, namespace={}", experimentId, userId, namespace);
                }
            } else {
                log.warn("未找到与演练ID关联的压测任务: experimentId={}", experimentId);
            }

            return null;

        } catch (Exception e) {
            log.error("根据演练ID查找压测任务失败: experimentId={}", experimentId, e);
            return null;
        }
    }
}
