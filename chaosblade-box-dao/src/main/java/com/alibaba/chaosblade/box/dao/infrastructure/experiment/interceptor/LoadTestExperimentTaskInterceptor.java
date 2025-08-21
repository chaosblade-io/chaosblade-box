package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.dao.command.ExperimentExecutionCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentTaskInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.LoadTestTaskManager;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.alibaba.chaosblade.box.dao.repository.LoadTestStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 压测演练任务拦截器
 * 在演练任务创建时检查是否需要启动压测，并在适当时机启动压测
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Component
public class LoadTestExperimentTaskInterceptor implements ExperimentTaskInterceptor {

    @Resource
    private LoadTestStrategyRepository loadTestStrategyRepository;

    @Resource
    private LoadTestTaskManager loadTestTaskManager;

    @Resource
    private CommandBus commandBus;

    @Override
    public void beforeSaveExperimentTaskDO(ExperimentTaskCreateRequest experimentTaskCreateRequest,
                                           ExperimentTaskDO experimentTaskDO) {
        try {
            String experimentId = experimentTaskCreateRequest.getExperimentDO().getExperimentId();

            // 查询是否有关联的压测策略
            Optional<LoadTestStrategyDO> strategyOptional =
                    loadTestStrategyRepository.findByExperimentId(experimentId);
            log.info("检测到演练 {} 关联了压测策略 {}", experimentId, strategyOptional.isPresent());
            if (strategyOptional.isPresent()) {
                LoadTestStrategyDO strategy = strategyOptional.get();

                // 检查策略是否启用
                if (strategy.getEnable().equals(1)) {
                    int startBeforeFaultSec = strategy.getStartBeforeFaultSec();
                    log.info("检测到演练 {} 关联了压测策略 {}, 立即启动压测，故障注入将延迟{}秒",
                            experimentId, strategy.getStrategyId(), startBeforeFaultSec);

                    // 立即启动压测任务
                    startLoadTestImmediately(strategy, experimentTaskDO);
                }
            }

        } catch (Exception e) {
            log.error("压测演练任务拦截器处理失败", e);
            // 不抛出异常，避免影响演练任务创建
        }
    }

    /**
     * 立即启动压测任务
     * 简化方案：立即启动压测，让压测和演练并行运行
     * startBeforeFaultSec 参数用于压测引擎内部的预热时间控制
     */
    private void startLoadTestImmediately(LoadTestStrategyDO strategy, ExperimentTaskDO experimentTaskDO) {
        CompletableFuture.runAsync(() -> {
            try {
                int startBeforeFaultSec = strategy.getStartBeforeFaultSec();

                log.info("立即启动压测任务，与演练并行运行，演练任务ID: {}", experimentTaskDO.getTaskId());

                // 立即启动压测任务
                Response<LoadTestTaskDO> response = loadTestTaskManager.createAndStartLoadTestTask(
                        strategy.getStrategyId(),
                        experimentTaskDO.getTaskId(),
                        experimentTaskDO.getUserId(),
                        experimentTaskDO.getNamespace()
                );

                if (response.isSuccess()) {
                    log.info("压测任务启动成功: taskId={}, experimentTaskId={}, 压测预热时间: {}秒",
                            response.getResult().getTaskId(), experimentTaskDO.getTaskId(), startBeforeFaultSec);
                } else {
                    log.error("压测任务启动失败: experimentTaskId={}, error={}",
                            experimentTaskDO.getTaskId(), response.getError().getErrorMessage());

                    // 如果配置了压测失败时中止演练，则需要停止演练
                    if (strategy.getAbortOnLoadFailure().equals(1)) {
                        log.warn("压测启动失败且配置了中止演练，需要停止演练任务: {}",
                                experimentTaskDO.getTaskId());
                        // TODO: 实现停止演练的逻辑
                    }
                }
                
            } catch (Exception e) {
                log.error("压测启动失败: experimentTaskId={}", experimentTaskDO.getTaskId(), e);
            }
        });
    }


}
