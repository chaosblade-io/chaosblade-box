package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.LoadTestTaskManager;
import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.alibaba.chaosblade.box.dao.repository.LoadTestTaskRepository;
import com.alibaba.chaosblade.box.service.LoadTestMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 压测监控服务实现类
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Service
public class LoadTestMonitorServiceImpl implements LoadTestMonitorService {

    @Resource
    private LoadTestTaskRepository loadTestTaskRepository;

    @Resource
    private LoadTestTaskManager loadTestTaskManager;

    /**
     * 定时监控压测任务状态
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    @Async
    public void scheduledMonitor() {
        try {
            log.debug("开始定时监控压测任务状态");
            monitorRunningLoadTestTasks();
        } catch (Exception e) {
            log.error("定时监控压测任务失败", e);
        }
    }

    /**
     * 定时处理超时任务
     * 每5分钟执行一次
     */
    @Scheduled(fixedRate = 300000)
    @Async
    public void scheduledTimeoutHandler() {
        try {
            log.debug("开始定时处理超时压测任务");
            handleTimeoutLoadTestTasks();
        } catch (Exception e) {
            log.error("定时处理超时压测任务失败", e);
        }
    }

    @Override
    public Response<String> monitorRunningLoadTestTasks() {
        try {
            // 查询所有正在运行的压测任务
            List<LoadTestTaskDO> allRunningTasks = loadTestTaskRepository.findAll("", "");
            
            int monitoredCount = 0;
            int syncedCount = 0;
            
            for (LoadTestTaskDO task : allRunningTasks) {
                if (task.isRunning()) {
                    monitoredCount++;
                    
                    // 异步同步任务状态
                    CompletableFuture.runAsync(() -> {
                        try {
                            loadTestTaskManager.syncLoadTestTaskStatus(task.getTaskId());
                        } catch (Exception e) {
                            log.warn("同步压测任务状态失败: taskId={}", task.getTaskId(), e);
                        }
                    });
                    
                    syncedCount++;
                }
            }
            
            String result = String.format("监控完成，共监控 %d 个运行中的压测任务，同步 %d 个任务状态", 
                    monitoredCount, syncedCount);
            log.debug(result);
            
            return Response.okWithData(result);
            
        } catch (Exception e) {
            log.error("监控运行中的压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "监控失败: " + e.getMessage());
        }
    }

    @Override
    public Response<String> monitorUserLoadTestTasks(String userId, String namespace) {
        try {
            List<LoadTestTaskDO> userRunningTasks = loadTestTaskRepository.findRunningTasks(userId, namespace);
            
            int monitoredCount = 0;
            
            for (LoadTestTaskDO task : userRunningTasks) {
                try {
                    loadTestTaskManager.syncLoadTestTaskStatus(task.getTaskId());
                    monitoredCount++;
                } catch (Exception e) {
                    log.warn("同步用户压测任务状态失败: userId={}, taskId={}", 
                            userId, task.getTaskId(), e);
                }
            }
            
            String result = String.format("用户 %s 的压测任务监控完成，共监控 %d 个任务", 
                    userId, monitoredCount);
            log.debug(result);
            
            return Response.okWithData(result);
            
        } catch (Exception e) {
            log.error("监控用户压测任务失败: userId={}", userId, e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "监控失败: " + e.getMessage());
        }
    }

    @Override
    public Response<String> handleTimeoutLoadTestTasks() {
        try {
            // 这里可以根据需要查询特定用户或所有用户的任务
            // 为了简化，先处理所有用户的超时任务
            Response<Integer> handleResponse = loadTestTaskManager.handleTimeoutTasks("", "");
            
            if (handleResponse.isSuccess()) {
                String result = String.format("超时任务处理完成，共处理 %d 个任务", handleResponse.getResult());
                log.debug(result);
                return Response.okWithData(result);
            } else {
                return Response.failedWith(handleResponse.getError());
            }
            
        } catch (Exception e) {
            log.error("处理超时压测任务失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "处理超时任务失败: " + e.getMessage());
        }
    }
}
